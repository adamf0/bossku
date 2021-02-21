package app.adam.bossku.helper;

public class StatusFunding {
    public static int BELUM_CHECKOUT=0;
    public static int MENUNGGU_PEMBAYARAN=1;
    public static int USER_CANCELLED=2;
    public static int TIME_OUT_PAYMENT=3;
    public static int VERIFY_PAYMENT=4;
    public static int PAYMENT_VERIFIED=5;

    public StatusFunding(){

    }
    public static String getStatus(int status){
        if(status==BELUM_CHECKOUT){
            return "Belum Bayar";
        }
        else if(status==MENUNGGU_PEMBAYARAN){
            return "Menunggu Pembayaran";
        }
        else if(status==USER_CANCELLED){
            return "Dibatalkan";
        }
        else if(status==TIME_OUT_PAYMENT){
            return "Waktu Pembayaran Habis";
        }
        else if(status==VERIFY_PAYMENT){
            return "Menunggun Verifikasi Pembayaran";
        }
        else if(status==PAYMENT_VERIFIED){
            return "Verifikasi Pembayaran";
        }
        return "N/a";
    }
}
