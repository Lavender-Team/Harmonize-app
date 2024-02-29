package kr.ac.chungbuk.harmonize.service;

import android.database.sqlite.SQLiteDatabase;

import kr.ac.chungbuk.harmonize.config.AppContext;
import kr.ac.chungbuk.harmonize.config.DatabaseHelper;
import kr.ac.chungbuk.harmonize.entity.SearchHistory;

public class SearchHistoryService {

    private static DatabaseHelper helper = new DatabaseHelper(
            AppContext.getAppContext(), "harmonize", null, 1
    );

    /**
     * @param history 검색 기록
     * 검색 기록(검색어, 생성 일시)를 데이터베이스에 저장합니다.
     */
    public static void save(SearchHistory history) {
        SQLiteDatabase database = helper.getWritableDatabase();
        // 같은 검색어가 존재하면 삭제
        database.execSQL("DELETE FROM search_history WHERE keyword = '" + history.getKeyword() + "';");

        database.execSQL(
                "INSERT INTO search_history(keyword, created_at) VALUES ('" +
                        history.getKeyword() + "', '" +
                        history.getCreatedAt().toString() + "');"
        );
        database.close();
    }

}
