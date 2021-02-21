package app.adam.bossku;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiConfig {
    @POST
    @FormUrlEncoded
    Call<String>request_login(
            @Url String url,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST
    @FormUrlEncoded
    Call<String>request_register(
            @Url String url,
            @Field("id_role") String id_role,
            @Field("name") String name,
            @Field("password") String password,
            @Field("email") String email,
            @Field("phone") String phone
    );

    @GET
    Call<String>request_goods_list_random(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_cart_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_cart_plus_minus(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_transaction_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_cart_add(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_goods_detail(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_goods_category(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_bank_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );


    @POST
    @FormUrlEncoded
    Call<String>request_user_bank_delete(
            @Url String url,
            @Field("id") String id,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_bank_add(
            @Url String url,
            @Field("bank_name") String bank_name,
            @Field("account_number") String account_number,
            @Field("account_name") String account_name,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_projects_detail(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_userdata(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_transaction_detail(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_master_data_category_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_shipping_address_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_rajaongkir_get_postalcode(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_transaction_pre_checkout(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_transaction_checkout(
            @Url String url,
            @Field("data") String data,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_funding_make(
            @Url String url,
            @Field("channel_id_or_code") String channel_id_or_code,
            @Field("id_crowd_funding") String id_crowd_funding,
            @Field("jumlah_donasi") String jumlah_donasi,
            @Field("payment_channel") String payment_channel,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_projects_list_onprogress(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_funding_my_donation(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @Multipart
    @POST
    Call<String> request_user_transaction_reciept_input(
            @Url String url,
            @Part MultipartBody.Part file_upload,
            @Part("transaction_code") RequestBody transaction_code,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_transaction_cancel(
            @Url String url,
            @Field("transaction_code") String transaction_code,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_bank_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_courier_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_rajaongkir_shipping_cost(
            @Url String url,
            @Field("origin") String origin,
            @Field("destination") String destination,
            @Field("weight") String weight,
            @Field("courier") String courier,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_master_data_provinces_list(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_master_data_cities_listby(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_master_data_subdistricts_listby(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_shipping_address_add_edit(
            @Url String url,
            @Field("name") String name,
            @Field("address") String addressaddress,
            @Field("province_id") String province_id,
            @Field("city_id") String city_id,
            @Field("subdistrict_id") String subdistrict_id,
            @Field("postal_code") String postal_code,
            @Field("is_default_address") String is_default_address,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_userprofile(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_umkmprofile(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_collegerprofile(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<ResponseBody>request_user_umkm_ktpimage(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<ResponseBody>request_user_umkm_ktmimage(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<ResponseBody>request_user_avatar(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<ResponseBody>request_user_logo(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_goods_add(
            @Url String url,
            @Field("name") String name,
            @Field("category") String category,
            @Field("price") String price,
            @Field("stock") String stock,
            @Field("weight") String weight,
            @Field("description") String description,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_goods_edit(
            @Url String url,
            @Field("name") String name,
            @Field("category") String category,
            @Field("price") String price,
            @Field("stock") String stock,
            @Field("weight") String weight,
            @Field("description") String description,
            @Field("id") String id,
            @HeaderMap Map<String, String> headers
    );

    @Multipart
    @POST
    Call<String> request_user_edit(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Part MultipartBody.Part avatar,
            @Part("sex") RequestBody sex,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("province_id") RequestBody province_id,
            @Part("city_id") RequestBody city_id,
            @Part("subdistrict_id") RequestBody subdistrict_id);

    @POST
    @FormUrlEncoded
    Call<String> request_user_edit64(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Field("avatar") String avatar,
            @Field("sex") String sex,
            @Field("date_of_birth") String date_of_birth,
            @Field("province_id") String province_id,
            @Field("city_id") String city_id,
            @Field("subdistrict_id") String subdistrict_id);

    @POST
    Call<String>request_user_shipping_address_delete(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @POST
    Call<String>request_user_shipping_address_change_default(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @GET
    Call<String>request_user_goods(
            @Url String url,
            @HeaderMap Map<String, String> headers
    );

    @POST
    @FormUrlEncoded
    Call<String>request_user_goods_delete(
            @Url String url,
            @Field("id") String id,
            @HeaderMap Map<String, String> headers
    );

    @Multipart
    @POST
    Call<String> request_umkm_colleger_update(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Part("ktm_number") RequestBody ktm_number,
            @Part("campuss") RequestBody campuss,
            @Part("department") RequestBody department,
            @Part("semester") RequestBody semester,
            @Part MultipartBody.Part ktm);

    @Multipart
    @POST
    Call<String> request_umkm_edit(
            @Url String url,
            @HeaderMap Map<String, String> headers,
            @Part("brand_name") RequestBody brand_name,
            @Part("ktp_number") RequestBody ktp_number,
            @Part MultipartBody.Part logo,
            @Part MultipartBody.Part ktp);

    @Multipart
    @POST
    @FormUrlEncoded
    Call<String> requestMultiFile(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> params, @Part List<MultipartBody.Part> files);
}