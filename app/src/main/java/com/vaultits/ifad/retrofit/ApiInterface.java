package com.vaultits.ifad.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    //endpoint for user login
    @Multipart
    @POST("UserAuth/login")
    Call<ResponseBody> UserLogin(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("Activities/Trackit")
    Call<ResponseBody> Trackit(
            @Part("vehicle") RequestBody vehicle
    );


    @Multipart
    @POST("Activities/Chekout")
    Call<ResponseBody> checkout(
            @Part("username") RequestBody username

            );

    @Multipart
    @POST("Activities/Chekin")
    Call<ResponseBody> checkin(
            @Part("username") RequestBody username

    );

    @Multipart
    @POST("Activities/Tripupdate")
    Call<ResponseBody> ACtv(
            @Part("appointments") RequestBody appointments
    );

    @Multipart
    @POST("Activities/Bus")
    Call<ResponseBody> Bus(
            @Part("appointments") RequestBody appointments
    );

    //endpoint to sync activities
    @Multipart
    @POST("Activities/SavePreuse")
    Call<ResponseBody> SyncActivities(
            @Part("appointments") RequestBody appointments
    );

    @Multipart
    @POST("Activities/PreTripMec")
    Call<ResponseBody> SyncActivit(
            @Part("appointments") RequestBody appointments
    );

    @Multipart
    @POST("Activities/Security")
    Call<ResponseBody> SyncSec(
            @Part("appointments") RequestBody appointments
    );


    @Multipart
    @POST("Activities/PreTripElec")
    Call<ResponseBody> SyncActivi(
            @Part("appointments") RequestBody appointments
    );

    @Multipart
    @POST("Activities/PreTripty")
    Call<ResponseBody> SyncActiv(
            @Part("appointments") RequestBody appointments
    );
    //GET LOOK UP DATA ENDPOINTS
    //endpoint for retrieving districts
    @GET("GetLookUpData/GetDistricts")
    Call<ResponseBody> getDistricts();

    @GET("GetLookUpData/GetActivities")
    Call<ResponseBody> getrips();

    //endpoint for retrieving districts
    @GET("GetLookUpData/GetFileTypes")
    Call<ResponseBody> getFileTypes();

    //endpoint for retrieving provinces
    @GET("GetLookUpData/GetProvinces")
    Call<ResponseBody> getProvincies();

    //endpoint for retrieving projects
    @GET("GetLookUpData/GetProjects")
    Call<ResponseBody> GetProjects();

    //endpoint for retrieving organisations
    @GET("GetLookUpData/GetOrganisations")
    Call<ResponseBody> GetOrganisations();

    //endpoint for retrieving components
    @GET("GetLookUpData/GetComponents")
    Call<ResponseBody> GetComponents();

    //endpoint for retrieving sub-components
    @GET("GetLookUpData/GetSubComponents")
    Call<ResponseBody> GetSubComponents();

    //endpoint for retrieving activities
    @GET("GetLookUpData/GetActivities")
    Call<ResponseBody> GetActivities();

    //endpoint for retrieving activities status
    @GET("GetLookUpData/GetActivityStatus")
    Call<ResponseBody> GetActivityStatus();

    //endpoint for retrieving facilitators
    @GET("GetLookUpData/GetFacilitators")
    Call<ResponseBody> GetFacilitators();

    //endpoint for retrieving budget activity list
    @GET("GetLookUpData/GetBudgetList")
    Call<ResponseBody> GetBudgetList();

    //endpoint for retrieving project districts
    @GET("GetLookUpData/GetProjectDistricts")
    Call<ResponseBody> GetProjectDistricts();

    //endpoint for retrieving irrigation schemes
    @GET("GetLookUpData/GetSchemes")
    Call<ResponseBody> GetSchemes();

    //endpoint
    @GET("GetLookUpData/GetSubComponentActivities")
    Call<ResponseBody> GetSubComponentActivities();

    //endpoint
    @GET("GetLookUpData/GetWards")
    Call<ResponseBody> GetWards();

    //endpoint
    @GET("GetLookUpData/GetGender")
    Call<ResponseBody> GetGender();

    //endpoint for posting file
    @Multipart
    @POST("Activities/SaveAppointmentFile")
    Call<ResponseBody> PostFile(
            @Part("act_topic") RequestBody act_topic,
            @Part("act_start") RequestBody act_start,
            @Part("act_end") RequestBody act_end,
            @Part("act_facilitator") RequestBody act_facilitator,
            @Part("act_des") RequestBody act_des,
            @Part("act_act_id") RequestBody act_act_id,
            @Part("act_site") RequestBody act_site,
            @Part("act_status") RequestBody act_status,
            @Part("act_project") RequestBody act_project,
            @Part("act_org") RequestBody act_org,
            @Part("act_dis") RequestBody act_dis,
            @Part("file_type") RequestBody file_type,
            @Part("file_title") RequestBody file_title,
            @Part("file_author") RequestBody file_author,
            @Part("file_des") RequestBody file_des,
            @Part("file_date") RequestBody file_date,
            @Part("file_author_org") RequestBody file_author_org,
            @Part("file_created_by") RequestBody file_created_by,
            @Part("file_extension") RequestBody extension,
            @Part MultipartBody.Part file,
            @Part("act_scheme") RequestBody act_scheme
    );
}
