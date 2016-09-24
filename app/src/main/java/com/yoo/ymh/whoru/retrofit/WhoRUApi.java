package com.yoo.ymh.whoru.retrofit;

import com.yoo.ymh.whoru.model.AppAlarmList;
import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.AppContactList;
import com.yoo.ymh.whoru.model.AppGroup;
import com.yoo.ymh.whoru.model.AppGroupItem;
import com.yoo.ymh.whoru.model.AppGroupList;
import com.yoo.ymh.whoru.model.AppUser;
import com.yoo.ymh.whoru.model.FcmToken;
import com.yoo.ymh.whoru.model.RemovedAppContactList;
import com.yoo.ymh.whoru.view.activity.DetailContactActivity;
import com.yoo.ymh.whoru.view.activity.ModifyGroupMemberActivity;
import com.yoo.ymh.whoru.view.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Yoo on 2016-09-01.
 */
public interface WhoRUApi {
    String QUERY_SESSIONID = "_sessionId";

    @POST("contact")          //내 로컬 전화부에서 서버 연락처 목록으로
    Observable<Integer> sendLocalcontactList(@Query(QUERY_SESSIONID)String sessionId, @Body AppContactList appContactList);

    @GET("contact")                 //내가 앱에 등록한 연락처 전부 (링크된친구 & 링크안된친구(localMember))
    Observable<AppContactList> getAllContactList(@Query(QUERY_SESSIONID)String sessionId);

    @GET("contact/detail")          //상세정보
    Observable<AppContact> getLocalDetailContact(@Query(QUERY_SESSIONID)String sessionId , @Query("_id")int id);

    @GET("group")                   //그룹리스트
    Observable<AppGroupList> getGroupList(@Query(QUERY_SESSIONID)String sessionId);
//    @Part("_group") ArrayList<Integer> groupList

    @Multipart
    @PUT("contact/detail")          //연락처 추가 수정 ->> 문제가 많음 확실히
    Observable<String> addOrModifyContact(@Query(QUERY_SESSIONID)String sessionId , @Part("_group") ArrayList<Integer> groupList, @PartMap Map<String,RequestBody> map ,
                                          @Part MultipartBody.Part cardImage,
                                          @Part MultipartBody.Part cardImage_back,
                                          @Part MultipartBody.Part profileImage);

    //사진 삭제
    @HTTP(method = "DELETE", path = "contact/image", hasBody = true)
    Observable<String> deleteContactImage(@Query(QUERY_SESSIONID)String sessionId, @Body AppContact appContact);

    //연락처삭제
    @HTTP(method = "DELETE", path = "contact", hasBody = true)
    Observable<String> deleteContact(@Query(QUERY_SESSIONID)String sessionId, @Body RemovedAppContactList removedAppContactList);

    @PUT("group/member")               //그룹원수정
    Observable<String> modifyGroupMemberList(@Query(QUERY_SESSIONID)String sessionId, @Body ModifyGroupMemberActivity.ModifyGroupMember modifyGroupMember);
//    @GET("group/contact")
//
    @GET("group/contact")
    Observable<AppGroupList> getGroupAndMemberList(@Query(QUERY_SESSIONID)String sessionId);

    @POST("group")            //그룹 추가
    Observable<String> addGroup(@Query(QUERY_SESSIONID)String sessionId, @Body AppGroup appGroup);

    @HTTP(method = "DELETE", path = "group", hasBody = true)          //그룹삭제
    Observable<String> deleteGroup(@Query(QUERY_SESSIONID)String sessionId, @Body GroupFragment.DeleteGroupId group);

    @PUT("contact/memo")
    Observable<String> modifyMemo(@Query(QUERY_SESSIONID)String sessionId, @Body AppContact modifyMemoContact);

    @PUT("contact/group")
    Observable<String> modifyGroup(@Query(QUERY_SESSIONID)String sessionId, @Body DetailContactActivity.ModifyGroup modifyGroupContact);

    @PUT("group")
    Observable<String> modifyGroupName(@Query(QUERY_SESSIONID)String sessionId, @Body AppGroupItem.ModifyGroupName modifyGroupName);

    @PUT("auth/login")
    Observable<AppUser> loginOrJoinUser(@Body AppContact appContact);

    @GET("info")
    Observable<AppContact> getMyInfo(@Query(QUERY_SESSIONID) String sessionId);

    @Multipart
    @PUT("info")
    Observable<String> modifyMyInfo(@Query(QUERY_SESSIONID) String sessionId, @PartMap Map<String,RequestBody> map ,
                                    @Part MultipartBody.Part cardImage,
                                    @Part MultipartBody.Part cardImage_back,
                                    @Part MultipartBody.Part profileImage );

    @HTTP(method = "DELETE", path = "info/image", hasBody = true)
    Observable<String> deleteContactMyImage(@Query(QUERY_SESSIONID)String sessionId, @Body AppContact appContact);

    @GET("unregister")
    Observable<String> unregisterMyContact(@Query(QUERY_SESSIONID) String sessionId);

    @PUT("fcm")
    Observable<Integer> sendFcmToken(@Query(QUERY_SESSIONID) String sessionId, @Body FcmToken fcmToken);

    @GET("fcm")
    Observable<AppAlarmList> getAlarmList(@Query(QUERY_SESSIONID) String sessionId);
}

