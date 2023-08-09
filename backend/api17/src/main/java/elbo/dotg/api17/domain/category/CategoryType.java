package elbo.dotg.api17.domain.category;

public enum CategoryType {
    BOARD("BOARD", "c01");

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
