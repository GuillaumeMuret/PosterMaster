package com.murey.poster.postermaster.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.murey.poster.postermaster.model.database.dao.JuriesDao;
import com.murey.poster.postermaster.model.database.dao.JuryMembersDao;
import com.murey.poster.postermaster.model.database.dao.JuryProjectsDao;
import com.murey.poster.postermaster.model.database.dao.PostersDao;
import com.murey.poster.postermaster.model.database.dao.ProjectsDao;
import com.murey.poster.postermaster.model.database.dao.StudentMarksDao;
import com.murey.poster.postermaster.model.database.dao.SupervisorsDao;
import com.murey.poster.postermaster.model.database.dao.TeamsDao;
import com.murey.poster.postermaster.model.database.dao.UsersDao;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.JuryProjects;
import com.murey.poster.postermaster.model.database.tables.Posters;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;
import com.murey.poster.postermaster.model.database.tables.Supervisors;
import com.murey.poster.postermaster.model.database.tables.Teams;
import com.murey.poster.postermaster.model.database.tables.Users;

@Database(entities = {
            Juries.class,
            JuryMembers.class,
            JuryProjects.class,
            StudentMarks.class,
            Posters.class,
            Projects.class,
            Supervisors.class,
            Teams.class,
            Users.class,
        }
        , version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract JuriesDao juriesDao();
    public abstract JuryMembersDao juryMembersDao();
    public abstract JuryProjectsDao juryProjectsDao();
    public abstract StudentMarksDao studentMarksDao();
    public abstract PostersDao postersDao();
    public abstract ProjectsDao projectsDao();
    public abstract SupervisorsDao supervisorsDao();
    public abstract TeamsDao teamsDao();
    public abstract UsersDao usersDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DatabaseVocabulary.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public void deleteAll(){

        juriesDao().deleteAll(juriesDao().getAll());
        juryMembersDao().deleteAll(juryMembersDao().getAll());
        juryProjectsDao().deleteAll(juryProjectsDao().getAll());
        postersDao().deleteAll(postersDao().getAll());
        projectsDao().deleteAll(projectsDao().getAll());
        studentMarksDao().deleteAll(studentMarksDao().getAll());
        supervisorsDao().deleteAll(supervisorsDao().getAll());
        teamsDao().deleteAll(teamsDao().getAll());
        usersDao().deleteAll(usersDao().getAll());

    }

}