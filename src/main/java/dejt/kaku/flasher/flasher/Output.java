package dejt.kaku.flasher.flasher;

public class Output {

    private final SetupBoard board;
    private int state;

    public Output(SetupBoard board) {
        this.board = board;
    }

    public void flash() throws Exception{
        if (this.state == 0) {
            this.state++;
            this.board.getDefpin().setValue(1);
        } else {
            this.state = 0;
            this.board.getDefpin().setValue(0);
        }
    }

}
