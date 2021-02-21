package app.adam.bossku.helper;

public class StatusEcommerce {
    public static int BELUM_CHECKOUT=0;
    public static int MENUNGGU_PEMBAYARAN=1;
    public static int USER_CANCELLED=2;
    public static int TIME_OUT_PAYMENT=3;
    public static int VERIFY_PAYMENT=4;
    public static int PAYMENT_VERIFIED=5;
    public static int MENUNGGU_RESPON_PENJUAL=6;
    public static int DIPROSES_PENJUAL=7;
    public static int SEDANG_PENGIRIMAN=8;
    public static int PESANAN_TIBA=9;
    public static int TRANSAKSI_SELESAI=10;
    public static int DITOLAK_PENJUAL=11;
    public static int SALDO_DITERIMA=12;

    public StatusEcommerce(){

    }
    public String getStatusBank(int status){
        if(status>=5)
            return "Sudah Transfer";
        else
            return "Belum Transfer";
    }
    public String getStatus(int status){
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
        else if(status==MENUNGGU_RESPON_PENJUAL){
            return "Menunggu Dikirim Penjual";
        }
        else if(status==DIPROSES_PENJUAL){
            return "Sedang Diproses Penjual";
        }
        else if(status==SEDANG_PENGIRIMAN){
            return "Sedang Dikirim Ke Tujuan";
        }
        else if(status==PESANAN_TIBA){
            return "Pesanan Tiba";
        }
        else if(status==TRANSAKSI_SELESAI){
            return "Transaksi Selesai";
        }
        else if(status==DITOLAK_PENJUAL){
            return "Ditolak Penjual";
        }
        else if(status==SALDO_DITERIMA){
            return "Saldo Diterima";
        }
        return "N/a";
    }
}
