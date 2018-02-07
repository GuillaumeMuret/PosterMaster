package com.murey.poster.postermaster.communication.requests;

import android.graphics.Bitmap;

import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EseoService {

    String BASE_URL = "https://192.168.4.10/www/pfe/";
    String WEB_SERVICE = "webservice.php";

    @GET(WEB_SERVICE)
    Call<WebMessage> authenticateUser(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_PASSWORD) String password
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getProjectList(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getMyProjectList(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getJuryList(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getMyJuryList(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getJuryInformation(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_JURY) int juryId,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getProjectInformation(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_PROJECT) int projectId,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<String> getPosterOfProject(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_PROJECT) int projectId,
            @Query(ProtocolVocabulary.QUERY_KEY_STYLE) String style,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getNotesOfTeamMember(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_PROJECT) int projectId,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> postNewNoteForStudent(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_PROJECT) int projectId,
            @Query(ProtocolVocabulary.QUERY_KEY_STUDENT_ID) int studentId,
            @Query(ProtocolVocabulary.QUERY_KEY_NOTE) float note,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );

    @GET(WEB_SERVICE)
    Call<WebMessage> getRangeProjectList(
            @Query(ProtocolVocabulary.QUERY_KEY_PROCESS) String appProcess,
            @Query(ProtocolVocabulary.QUERY_KEY_USER) String username,
            @Query(ProtocolVocabulary.QUERY_KEY_SEED) int seedProjectId,
            @Query(ProtocolVocabulary.QUERY_KEY_TOKEN) String token
    );
}