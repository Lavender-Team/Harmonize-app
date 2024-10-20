package kr.ac.chungbuk.harmonize.config;

public class Domain {
    // AVD
    //public static String name = "http://10.0.2.2:8080";
    
    // IP 입력
    public static String name = "http://192.168.0.4:8080";

    // 테스트 URL
    //public static String name = "https://dev.harmonize.studio1122.net";

    /**
     * @param path 도메인 이름 이후 "/"로 시작하는 경로
     * @return 도메인 주소 + 경로
     */
    public static String url(String path)
    {
        return name + path;
    }
}
