package all.model;

public class Part {
    private final TopologyType type;
    private final int index;
    private final int count;//počet primitiv(těles)

    public Part(TopologyType type, int index, int count) {
        this.type = type;
        this.index = index;
        this.count = count;
    }

    public TopologyType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }
}
