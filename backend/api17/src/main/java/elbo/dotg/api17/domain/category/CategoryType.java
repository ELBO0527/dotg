package elbo.dotg.api17.domain.category;

import java.util.stream.Stream;

public enum CategoryType {
    BOARD_COMMON("전체게시판", "c01"),
    BOARD_NOTICE("공지사항게시판", "c02"),
    BOARD_COMMUNITY("커뮤니티게시판", "c03");

    private final String categoryName;
    private final String categoryCode;

    CategoryType(String categoryName, String categoryCode) {
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public String getCategoryCode(){
        return categoryCode;
    }
}