package elbo.dotg.api17.domain.category;

public enum CategoryType {
    BOARD_COMMON("board", "common","전체 게시판", "c01"),
    BOARD_NOTICE("board", "notice","공지사항 게시판", "c02"),
    BOARD_COMMUNITY("board", "community","커뮤니티 게시판", "c03");

    private final String typ;
    private final String name;
    private final String detail;
    private final String cd;

    CategoryType(String typ, String name, String detail, String cd) {
        this.typ = typ;
        this.name = name;
        this.detail = detail;
        this.cd = cd;
    }

    public String getTyp() {
        return typ;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getCd() {
        return cd;
    }
}