package kr.ac.chungbuk.harmonize.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import kr.ac.chungbuk.harmonize.config.AppContext;
import kr.ac.chungbuk.harmonize.config.DatabaseHelper;
import kr.ac.chungbuk.harmonize.dto.AuthDto;

public class AuthDao {

    private static DatabaseHelper helper = new DatabaseHelper(
            AppContext.getAppContext(), "harmonize", null, 1
    );

    /**
     * 로그인 정보(토큰, 일시, 유저 ID 등)을 데이터베이스에 저장합니다.
     * @param authDto 로그인 정보 Dto
     */
    public static void save(AuthDto authDto) {
        SQLiteDatabase database = helper.getWritableDatabase();
        // 이미 저장된 로그인 정보는 삭제
        database.execSQL("DELETE FROM auth;");

        database.execSQL(
                "INSERT INTO auth(token, user_id, nickname, gender, age, genre, created_at) VALUES ('" +
                    authDto.getToken() + "', " +
                    authDto.getUserId().toString() + ", '" +
                    authDto.getNickname() + "', '" +
                    (authDto.getGender() != null ? authDto.getGender() : "") + "', " +
                    (authDto.getAge() != null ? authDto.getAge().toString() : 0) + ", '" +
                    authDto.getGenre().toString() + "', '" +
                    authDto.getCreatedAt().toString() + "');"
        );

        database.close();
    }

    public static AuthDto find() {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT token, user_id, nickname, gender, age, genre, created_at FROM auth",
                null
        );

        if (cursor.getCount() <= 0) {
            // 저장된 토큰이 존재하지 않으면
            cursor.close();
            return null;
        }

        cursor.moveToNext();
        AuthDto authDto = new AuthDto();
        authDto.setToken(cursor.getString(0));
        authDto.setUserId(cursor.getLong(1));
        authDto.setNickname(cursor.getString(2));
        authDto.setGender(cursor.getString(3));
        authDto.setAge(cursor.getInt(4));
        authDto.setGenre(parseList(cursor.getString(5)));
        authDto.setCreatedAt(LocalDateTime.parse(cursor.getString(6)));

        return authDto;
    }


    private static List<String> parseList(String s) {
        List<String> output = new ArrayList<>();
        String listString = s.substring(1, s.length() - 1);
        StringTokenizer st = new StringTokenizer(listString, ",");

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            output.add(token.trim());
        }
        return output;
    }
}
