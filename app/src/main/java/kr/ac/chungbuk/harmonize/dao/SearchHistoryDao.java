package kr.ac.chungbuk.harmonize.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kr.ac.chungbuk.harmonize.config.AppContext;
import kr.ac.chungbuk.harmonize.config.DatabaseHelper;
import kr.ac.chungbuk.harmonize.entity.SearchHistory;

public class SearchHistoryDao {

    private static DatabaseHelper helper = new DatabaseHelper(
            AppContext.getAppContext(), "harmonize", null, 1
    );

    /**
     * 검색 기록(검색어, 생성 일시)를 데이터베이스에 저장합니다.
     * @param history 검색 기록
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

    /**
     * 데이터베이스에서 검색 기록을 조회합니다.
     * @param limit 조회할 검색 기록의 개수 제한
     * @return 조회된 검색 기록 ArrayList
     */
    public static List<SearchHistory> findAll(Integer limit) {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT keyword, created_at FROM search_history ORDER BY created_at DESC LIMIT " + limit.toString(),
                null
        );

        List<SearchHistory> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            String keyword = cursor.getString(0);
            String createdAt = cursor.getString(1);

            results.add(new SearchHistory(
                    keyword,
                    LocalDateTime.parse(createdAt)
            ));
        }
        return results;
    }

    /**
     * 데이터베이스에서 특정 검색어 기록을 삭제합니다.
     * @param keyword 삭제할 검색어
     */
    public static void delete(String keyword) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("DELETE FROM search_history WHERE keyword = '" + keyword + "';");
        database.close();
    }
}
