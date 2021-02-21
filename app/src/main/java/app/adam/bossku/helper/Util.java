package app.adam.bossku.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import app.adam.bossku.R;
import app.adam.bossku.view.model.Alamat;
import app.adam.bossku.view.model.Bank;
import app.adam.bossku.view.model.ItemKeranjang;
import app.adam.bossku.view.model.Keranjang;

public class Util {
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }
    @SuppressLint("SetTextI18n")
    public static Drawable buildCounterDrawable(Activity act, int layout, int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(act);
        View view = inflater.inflate(layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(act.getResources(), bitmap);
    }
    public static DecimalFormat rupiahFormat(){
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(formatRp);
        return decimalFormat;
    }
    public static String rupiahFormat(BigDecimal nilai){
        return rupiahFormat().format(nilai);
    }
    public static String rupiahFormat(int nilai){
        return rupiahFormat().format(nilai);
    }
    public static String rupiahFormat(Double nilai){
        return rupiahFormat().format(nilai);
    }

//    public static String errorVolley(VolleyError volleyError){
//        if (volleyError instanceof NetworkError) {
//            return "Cannot connect to Internet...Please check your connection!";
//        } else if (volleyError instanceof ServerError) {
//            return "The server could not be found. Please try again after some time!!";
//        } else if (volleyError instanceof AuthFailureError) {
//            return "Cannot connect to Internet...Please check your connection!";
//        } else if (volleyError instanceof ParseError) {
//            return "Parsing error! Please try again after some time!!";
//        } else if (volleyError instanceof NoConnectionError) {
//            return "Cannot connect to Internet...Please check your connection!";
//        } else if (volleyError instanceof TimeoutError) {
//            return "Connection TimeOut! Please check your internet connection.";
//        }
//        else{
//            return "Error";
//        }
//    }

    public static JSONArray parsingKeranjang(ArrayList<Keranjang> list_keranjang, String data){
        JSONArray productId = new JSONArray();
        JSONArray goodPrice = new JSONArray();
        JSONArray courierId = new JSONArray();
        JSONArray courierType = new JSONArray();
        JSONArray totalCost = new JSONArray();
        JSONArray totalPrice = new JSONArray();
        JSONArray userMessage = new JSONArray();

        if(list_keranjang!=null) {
            for (int i = 0; i < list_keranjang.size(); i++) {
                Keranjang keranjang = list_keranjang.get(i);
                ArrayList<ItemKeranjang> itemKeranjang = keranjang.getItemKeranjang();
                for (int j = 0; j < itemKeranjang.size(); j++) {
                    ItemKeranjang x = itemKeranjang.get(j);
                    productId.put(x.getId());
                    goodPrice.put(String.valueOf(x.getHarga_satuan()));
                    courierId.put(x.getKurir().getId());
                    courierType.put(x.getKurir().getService());
                    totalCost.put(String.valueOf(x.getKurir().getPrice()));
                    totalPrice.put(String.valueOf(x.getHarga_satuan()*x.getJumlah_beli()));
                    userMessage.put(x.getCatatan());
                }
            }

            switch (data) {
                case "product_id":
                    return productId;
                case "good_price":
                    return goodPrice;
                case "courier_id":
                    return courierId;
                case "courier_type":
                    return courierType;
                case "total_cost":
                    return totalCost;
                case "total_price":
                    return totalPrice;
                default:
                    return userMessage;
            }
        }
        else{
            return null;
        }
    }
    public static JSONObject dataRequestPembayaranBarang(String invoice_no, Alamat alamat, String payment_channel, Bank bank,ArrayList<Keranjang> list_keranjang){
        JSONObject obj = new JSONObject();

        if(list_keranjang!=null) {
            try {
                obj.put("invoice_no",invoice_no);
                obj.put("shipping_address",alamat.getKecamatan().getId());
                obj.put("payment_channel",payment_channel);
                obj.put("channel_id_or_code",bank.getId());

                JSONArray data = new JSONArray();
                for (int i = 0; i < list_keranjang.size(); i++) {
                    Keranjang keranjang = list_keranjang.get(i);
                    double total_ongkir=0;
                    int berat_total=0;
                    JSONArray data_product = new JSONArray();

                    ArrayList<ItemKeranjang> itemKeranjang = keranjang.getItemKeranjang();
                    for (int j = 0; j < itemKeranjang.size(); j++) {
                        ItemKeranjang x = itemKeranjang.get(j);
                        berat_total += (x.getBerat() * x.getJumlah_beli());
                        total_ongkir += x.getKurir().getPrice() * Math.ceil((double) berat_total / 1000);

                        JSONObject objDataProduct = new JSONObject();
                        objDataProduct.put("product_id",x.getId());
                        objDataProduct.put("good_price",x.getHarga_satuan());
                        objDataProduct.put("quantity",x.getJumlah_beli());
                        objDataProduct.put("total_price",x.getHarga_satuan()*x.getJumlah_beli());
                        //objDataProduct.put("user_message",x.getCatatan());
                        data_product.put(objDataProduct);

                        if(j==itemKeranjang.size()-1) {
                            JSONObject objData = new JSONObject();
                            objData.put("courier", x.getKurir().getPrice());
                            objData.put("courier", x.getKurir().getId());
                            objData.put("courier_type", x.getKurir().getService());
                            objData.put("total_cost", (x.getHarga_satuan() * x.getJumlah_beli()) + total_ongkir);
                            objData.put("user_message", keranjang.getCatatan());
                            objData.put("data_product",data_product);

                            data.put(objData);
                        }
                    }
                }
                obj.put("data",data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }
    public static String convert_date(String date, String from_format, String to_format){
        String newDate = null;
        SimpleDateFormat format = new SimpleDateFormat(from_format,new Locale("in","ID"));
        SimpleDateFormat dates = new SimpleDateFormat(to_format,new Locale("in","ID"));

        try {
            newDate = dates.format(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
//    public static void openCropActivity(Activity act, Uri sourceUri, Uri destinationUri) {
//        UCrop.Options options = new UCrop.Options();
//        options.setCircleDimmedLayer(true);
//        options.setCropFrameColor(ContextCompat.getColor(act, R.color.colorAccent));
//        UCrop.of(sourceUri, destinationUri)
//                .withMaxResultSize(512, 512)
//                .withAspectRatio(4f, 4f)
//                .start(act);
//    }
    public static Bitmap getThumbnail(Activity act, Uri uri) throws IOException{
        InputStream input = act.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        double ratio = (originalSize > 512) ? (originalSize / 512) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = act.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
    public static void checkAndRequestPermissions(Activity act) {
        PermissionHelper permissionHelper = new PermissionHelper(act);
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {
                Log.i("app-log","akses permission berhasil");
            }
        });
        permissionHelper.checkAndRequestPermissions();

    }
}
