package dejt.kaku.flasher.flasher;

import dejt.kaku.flasher.LightFlasher;
import javafx.scene.effect.Light;
import net.minecraft.client.Minecraft;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import javax.swing.*;
import java.awt.*;

/**
 * Before you use this, you must have the Arduino IDE, then run StandardFirmata to the board. If your USB -> UART is a CH340, you
 * have to download driver from the web before you can use the board.<p>
 * Steps to run StandardFirmata: File -> Examples -> Firmata -> StandardFirmata. Click upload to board and wait.
 * </p>
 * The rest is just java and hardware. Connect the defpin to a transistor using a base resistor. The transistor will act as a switch.
 * The transistor will turn on and off a light bulb depending on the state of defpin.
 *
 * Also, you must indicate in lightbulbflasher.json settings your defpin and whether you allow no-board condition for running the mod.
 * The default for both is [13] and [true] for testing purpose. Feel free to change. But make sure the pin exists on your board.
 */
public class SetupBoard {

    private final Pin defpin;
    private boolean status;
    private final IODevice board;
    private Output output;

    public SetupBoard(int defpin) throws Exception{
        IODevice channel = new FirmataDevice("");
        String active_port = "";
        for(String port : LightFlasher.configuration.ports_available){
            channel = new FirmataDevice(port);
            try {
                channel.start();
                channel.ensureInitializationIsDone();
                if(channel.isReady()){
                    active_port = port;
                    System.out.println("Board on port name: " + active_port + " successfully connected");
                    this.status = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("Port name: " + port + " fail. Try next one...");
            }
        }
        if (active_port.equals("")) {
            System.out.println("Error! Connection failed for all ports.");
            this.status = LightFlasher.configuration.settings.noBoardAllowRun;
            if (!this.status) {
                throw new Exception("Abnormal program condition: Board missing! Check your ports and board.");
            } else {
                this.status = false;
                this.defpin = null;
                this.board = null;
                return;
            }
        }
        this.defpin = channel.getPin(defpin);
        this.board = channel;
        this.output = new Output(this);
    }

    public Pin getDefpin() {
        return this.defpin;
    }

    public IODevice getBoard() {
        return this.board;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void doAction() {
        try {
            this.output.flash();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void end() {
        try {
            this.board.stop();
            System.out.println("The board has been stopped successfully");
        } catch (Exception e) {
            LightFlasher.logger.error(e.getMessage());
        }
    }

}
