package app.adam.bossku.helper;

public class Route {
    public static String base_url = "https://server.bossku.id/systemcall";
    public static String storage = "https://server.bossku.id/storage/";

    public static String category_list = "/master_data/category/list";
    public static String funding_projetcs_list = "/projects/list/onprogress?filter=%s&limit=%s&page=%s&category=%s";
    public static String funding_project_detail = "/projects/detail/%s";
    public static String donation_detail = "/user/funding/donation_detail/%s";
    public static String is_user_has_login = "/user/userdata";
    public static String login = "/user/login";
    public static String register_user = "/user/register";
    public static String goods_list_random = "/goods/list/random";
    public static String goods_list_by_category = "/goods/category/%s";
    public static String good_detail = "/goods/detail/%s";
    public static String bank_list = "/bank/list";
    public static String courier_list = "/courier/list";
    public static String provinces_list = "/master_data/provinces/list";
    public static String cities_list = "/master_data/cities/listby/%s";
    public static String subdistricts_list = "/master_data/subdistricts/listby/%s";
    public static String get_postal_code = "/rajaongkir/get-postalcode/%s"; //kec
    //public static String user_contact = "/user/usercontact";
    public static String shipping_address_list = "/user/shipping_address/list";
    public static String shipping_address_add = "/user/shipping_address/add";
    public static String shipping_address_edit = "/user/shipping_address/edit/%s";
    public static String shipping_address_detail = "/user/shipping_address/detail/%s";
    public static String shipping_address_delete = "/user/shipping_address/delete/%s";
    public static String shipping_address_default = "/user/shipping_address/change_default";
    public static String shipping_address_cost = "/rajaongkir/shipping_cost";

    public static String list_cart = "/user/cart/list";
    public static String add_cart = "/user/cart/add/%s";
    public static String add_qty = "/user/cart/plus/%s";
    public static String min_qty = "/user/cart/minus/%s";
    public static String invoice_number = "/user/transaction/pre/checkout";
    public static String checkout = "/user/transaction/checkout";
    public static String transaction_list = "/user/transaction/list/%s?page=%s&limit=%s"; //rev
    public static String transaction_detail = "/user/transaction/detail/%s";
    public static String donate_send = "/user/funding/make";
    public static String donate_cancel = "/user/transaction/cancel";
    public static String donate_bill = "/user/transaction/reciept_input";
    public static String donate_list = "/user/funding/my_donation/%s?page=%s&limit=%s"; //rev
    public static String user_profile = "/user/userprofile";
    public static String user_edit = "/user/edit";
    public static String umkm_profile = "/user/umkmprofile";
    public static String ktp_image = "/user/umkm/ktpimage";
    public static String umkm_edit = "/user/umkm/edit";
    public static String umkm_colleger_data = "/user/collegerprofile";
    public static String ktm_image = "/user/umkm/ktmimage";
    public static String umkm_colleger_update = "/user/colleger/edit";
    public static String list_bank_account = "/user/bank/list?page=%s&limit=%s";
    public static String delete_bank_account = "/user/bank/delete";
    public static String add_bank_account = "/user/bank/add";
    public static String selfproduct = "/user/goods/selfproduct";
    public static String add_product = "/user/goods/add";
    public static String edit_product = "/user/goods/edit";
    public static String delete_product = "/user/goods/delete";


//    void provinces(){
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = Route.base_url+Route.provinces_list;
//
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            String status = res.getString("status");
//                            String message = res.getString("message");
//
//                            if(status.equals("success")){
//                                JSONArray datas = res.getJSONArray("data");
//                                for (int i=0;i<datas.length();i++){
//                                    JSONObject obj = datas.getJSONObject(i);
//                                    list_provinsi.add(
//                                            new Provinsi(
//                                                    obj.getString("province_id"),
//                                                    obj.getString("province")
//                                            )
//                                    );
//                                }
//                            }
//                            else{
//                                Toast.makeText(KategoriActivity.this,"Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(KategoriActivity.this, Util.errorVolley(volleyError), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                SessionManager session = new SessionManager(KategoriActivity.this);
//                HashMap<String, String> data = session.getSessionLogin();
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("cookie", data.get(SessionManager.KEY_TOKEN));
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5*60000, 20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
//    }
}
