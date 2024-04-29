public class VIPGäster extends Gäster {
    enum VIPStatus {EJ_VIP, BRONS, SILVER, GULD, DIAMANT};

    VIPStatus[] vipStatus = new VIPStatus[20];

    public VIPGäster() {
        super();
    }

    public void sättVIPStatus(int index, VIPStatus status) {
        vipStatus[index] = status;
    }

    public VIPStatus hämtaVIPStatus(int index) {
        return vipStatus[index];
    }
}
