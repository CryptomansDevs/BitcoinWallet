package wallet.bitcoin.bitcoinwallet.helper;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wallet.bitcoin.bitcoinwallet.model.DaoMaster;


public class UpgradeDb extends DaoMaster.OpenHelper {

    private interface Migration {
        Integer getVersion();

        void runMigration(Database db);
    }

    public UpgradeDb(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        List<Migration> migrations = getMigrations();

        // Only run migrations past the old version
        for (Migration migration : migrations) {
            if (oldVersion < migration.getVersion()) {
                migration.runMigration(db);
            }
        }
    }

    private List<Migration> getMigrations() {
        List<Migration> migrations = new ArrayList<>();
//        migrations.add(new MigrationV2());


        // Sorting just to be safe, in case other people add migrations in the wrong order.
        Comparator<Migration> migrationComparator = new Comparator<Migration>() {
            @Override
            public int compare(Migration m1, Migration m2) {
                return m1.getVersion().compareTo(m2.getVersion());
            }
        };
        Collections.sort(migrations, migrationComparator);

        return migrations;
    }

}

//    private static class MigrationV2 implements Migration {
//        @Override
//        public Integer getVersion() {
//            return 2;
//        }
//
//        @Override
//        public void runMigration(Database db) {
//            // Add new column to user table
//            db.execSQL("ALTER TABLE " + ServerConfigDao.TABLENAME + " ADD COLUMN "
//                    + ServerConfigDao.Properties.LastTimeCustomAdUpdate.columnName + " LONG DEFAULT 0");
//
//            db.execSQL("ALTER TABLE " + ServerConfigDao.TABLENAME + " ADD COLUMN "
//                    + ServerConfigDao.Properties.LastTimeConfigUpdated.columnName + " LONG DEFAULT 0");
//
//            db.execSQL("ALTER TABLE " + ServerConfigDao.TABLENAME + " ADD COLUMN "
//                    + ServerConfigDao.Properties.CustomAdVer.columnName + " INT DEFAULT -1");
//        }
//    }