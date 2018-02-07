/**
 * @file ProtocolVocabulary.java
 * @brief Refer the keys used on the JSON object
 *
 * @version 1.0
 * @date 06/05/16
 * @author François LEPAROUX
 * @collab Guillaume MURET
 * @copyright
 *	Copyright (c) 2016, PRØVE
 * 	All rights reserved.
 * 	Redistribution and use in source and binary forms, with or without
 * 	modification, are permitted provided that the following conditions are met:
 *
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of PRØVE, Angers nor the
 * 	  names of its contributors may be used to endorse or promote products
 *   	derived from this software without specific prior written permission.
 *
 * 	THIS SOFTWARE IS PROVIDED BY PRØVE AND CONTRIBUTORS ``AS IS'' AND ANY
 * 	EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * 	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * 	DISCLAIMED. IN NO EVENT SHALL PRØVE AND CONTRIBUTORS BE LIABLE FOR ANY
 * 	DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * 	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * 	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * 	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * 	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * 	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.murey.poster.postermaster.communication.requests.protocole;

public class ProtocolVocabulary {

    // Initial validation of appUserAccount credentials;
    public static final String PROCESS_AUTHENTICATE_USER = "LOGON";

    // List information on all projects;
    public static final String PROCESS_LIST_ALL_PROJECT = "LIPRJ";

    // List information on projects where the user is the supervisor;
    public static final String PROCESS_LIST_MY_PROJECT = "MYPRJ";

    // List information on all the juries;
    public static final String PROCESS_LIST_ALL_JURIES_INFO = "LIJUR";

    // List information on the juries where the user is a member of the jury;
    public static final String PROCESS_LIST_MY_JURIES_INFO = "MYJUR";

    // Provide detailed information on projects in a given jury;
    public static final String PROCESS_GET_PROJECT_JURIES_INFO = "JYINF";

    // Recuperate full information for a given project;
    public static final String PROCESS_GET_PROJECT_INFO = "PROJE";

    // Recuperate a poster for a given project;
    public static final String PROCESS_GET_POSTER_PROJECT = "POSTR";

    // List the ’notes’ of all the team members for a given project;
    public static final String PROCESS_LIST_MEMBER_PROJECT_NOTES = "NOTES";

    // Add or update a note for a given student;
    public static final String PROCESS_GIVE_NOTE_FOR_STUDENT = "NEWNT";

    // Recuperate a range of non confidential projects and posters for a demonstration “portes ouvertes”?;
    public static final String PROCESS_GET_RANGE_PROJECTS_POSTERS = "PORTE";


    public static final String QUERY_KEY_PROCESS = "q";
    public static final String QUERY_KEY_USER = "user";
    public static final String QUERY_KEY_JURY = "jury";
    public static final String QUERY_KEY_PROJECT = "proj";
    public static final String QUERY_KEY_PASSWORD = "pass";
    public static final String QUERY_KEY_TOKEN = "token";
    public static final String QUERY_KEY_STYLE = "style";
    public static final String QUERY_KEY_STYLE_VALUE_FULL = "FULL";
    public static final String QUERY_KEY_STYLE_VALUE_THUMB = "THUMB";
    public static final String QUERY_KEY_STYLE_VALUE_FLB64 = "FLB64";
    public static final String QUERY_KEY_STYLE_VALUE_THB64 = "THB64";
    public static final String QUERY_KEY_PROJECT_ID = "proj";
    public static final String QUERY_KEY_STUDENT_ID = "student";
    public static final String QUERY_KEY_NOTE = "note";
    public static final String QUERY_KEY_SEED = "seed";

    // Bundle value
    public static final String KEY_BUNDLE = "KEY_BUNDLE";
    public static final String BUNDLE_KEY_PROJECT_ID = "BUNDLE_KEY_PROJECT_ID";
    public static final String BUNDLE_KEY_PROJECT = "BUNDLE_KEY_PROJECT";
    public static final String BUNDLE_KEY_ROOT_SCREEN = "BUNDLE_KEY_ROOT_SCREEN";
    public static final String BUNDLE_KEY_USER = "BUNDLE_KEY_USER";
    public static final String BUNDLE_KEY_PASSWORD = "BUNDLE_KEY_PASSWORD";
    public static final String BUNDLE_KEY_MESSAGE = "message";
    public static final String BUNDLE_KEY_JURY_ID = "BUNDLE_KEY_JURY_ID";
    public static final String BUNDLE_KEY_JURY = "BUNDLE_KEY_JURY";
    public static final String BUNDLE_KEY_STUDENT_NOTE = "BUNDLE_KEY_STUDENT_NOTE";
    public static final String BUNDLE_KEY_STUDENT_ID = "BUNDLE_KEY_STUDENT_ID";

    /**
     * Keys of the json message
     */
    public static final String JSON_KEY_RESULT = "result";
    public static final String JSON_KEY_RESULT_VALUE_OK = "OK";
    public static final String JSON_KEY_RESULT_VALUE_KO = "KO";
    public static final String JSON_KEY_TOKEN = "token";
    public static final String JSON_KEY_API = "api";
    public static final String JSON_KEY_ERROR = "error";

    public static final String JSON_KEY_PROJECTS = "projects";
    public static final String JSON_KEY_MEMBERS = "members";
    public static final String JSON_KEY_PROJECT_ID = "projectId";
    public static final String JSON_KEY_ID_PROJECT = "idProject";
    public static final String JSON_KEY_PROJECT_TITLE = "title";
    public static final String JSON_KEY_PROJECT_DESCRIPTION = "descrip";
    public static final String JSON_KEY_PROJECT_POSTER_ENABLE = "poster";
    public static final String JSON_KEY_SUPERVISOR = "supervisor";
    public static final String JSON_KEY_PROJECT_CONFIDENTIALITY = "config";
    public static final String JSON_KEY_STUDENTS = "students";


    public static final String JSON_KEY_JURIES = "juries";
    public static final String JSON_KEY_JURY_ID = "idJury";
    public static final String JSON_KEY_DATE = "date";
    public static final String JSON_KEY_INFO = "info";

    public static final String JSON_KEY_NOTES = "notes";
    public static final String JSON_KEY_MY_NOTE = "mynote";
    public static final String JSON_KEY_AVERAGE_NOTE = "avgNote";

    public static final String JSON_KEY_USER_ID = "userId";
    public static final String JSON_KEY_FORENAME = "forename";
    public static final String JSON_KEY_SURNAME = "surname";


    /**
     * Possible constant results of the json message
     */
    public static final String JSON_RESULT_VALUE_OK = "OK";
    public static final String JSON_RESULT_VALUE_KO = "KO";

}
