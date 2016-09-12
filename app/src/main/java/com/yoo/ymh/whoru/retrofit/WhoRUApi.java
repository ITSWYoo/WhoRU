package com.yoo.ymh.whoru.retrofit;

import com.yoo.ymh.whoru.model.AppContact;
import com.yoo.ymh.whoru.model.AppContactList;
import com.yoo.ymh.whoru.model.Group;
import com.yoo.ymh.whoru.model.GroupItem;
import com.yoo.ymh.whoru.model.GroupList;
import com.yoo.ymh.whoru.model.RemovedContactList;
import com.yoo.ymh.whoru.view.activity.DetailContactActivity;
import com.yoo.ymh.whoru.view.activity.ModifyGroupMemberActivity;
import com.yoo.ymh.whoru.view.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;
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
    Observable<GroupList> getGroupList(@Query(QUERY_SESSIONID)String sessionId);
//    @Part("_group") ArrayList<Integer> groupList

    @Multipart
    @PUT("contact/detail")          //연락처 추가 수정 ->> 문제가 많음 확실히
    Observable<String> addOrModifyContact(@Query(QUERY_SESSIONID)String sessionId , @Part("_group") ArrayList<Integer> groupList, @PartMap Map<String,RequestBody> map ,
                                          @Part MultipartBody.Part cardImage,
                                          @Part MultipartBody.Part cardImage_back,
                                          @Part MultipartBody.Part profileImage);

    //연락처삭제 한명밖에안되자나씨벌
    @HTTP(method = "DELETE", path = "contact", hasBody = true)
    Observable<String> deleteContact(@Query(QUERY_SESSIONID)String sessionId, @Body RemovedContactList removedContactList);

    @PUT("group/member")               //그룹원수정
    Observable<String> modifyGroupMemberList(@Query(QUERY_SESSIONID)String sessionId, @Body ModifyGroupMemberActivity.ModifyGroupMember modifyGroupMember);
//    @GET("group/contact")
//
    @GET("group/contact")
    Observable<GroupList> getGroupAndMemberList(@Query(QUERY_SESSIONID)String sessionId);
    @POST("group")            //그룹 추가
    Observable<String> addGroup(@Query(QUERY_SESSIONID)String sessionId, @Body Group group);

    @HTTP(method = "DELETE", path = "group", hasBody = true)          //그룹삭제
    Observable<String> deleteGroup(@Query(QUERY_SESSIONID)String sessionId, @Body GroupFragment.DeleteGroupId group);

    @PUT("contact/memo")
    Observable<String> modifyMemo(@Query(QUERY_SESSIONID)String sessionId, @Body AppContact modifyMemoContact);

    @PUT("contact/group")
    Observable<String> modifyGroup(@Query(QUERY_SESSIONID)String sessionId, @Body DetailContactActivity.ModifyGroup modifyGroupContact);

    @PUT("group")
    Observable<String> modifyGroupName(@Query(QUERY_SESSIONID)String sessionId, @Body GroupItem.ModifyGroupName modifyGroupName);
}

