package com.abiyyu0003.asessment2mobpro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abiyyu0003.asessment2mobpro.model.Catatan

@Database(
    entities = [Catatan::class],
    version = 2,
    exportSchema = false
)
abstract class CatatanDb : RoomDatabase() {

    abstract val dao: CatatanDao

    companion object {

        @Volatile
        private var INSTANCE: CatatanDb? = null

        fun getInstance(context: Context): CatatanDb {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context,
                        CatatanDb::class.java,
                        "catatan.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}