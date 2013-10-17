package number;

public enum NumberPlaceState {

	OneAnser(1, "1つ解答がありました"),
	ManyAnser(2, "2つ以上の解答がありました"),
	
	NoAnser(99, "解答がありません"),
	
	NoExistFile(-1, "ファイルが存在しません"),
	CantReadFile(-2, "ファイルが存在しません"),
	NoFile(-3, "ファイルが存在しません"),
	NoExtension(-4, "ファイルが存在しません"),
	OtherExtension(-5, "テキストファイル(.txt)を指定してください"),
	IllegalLineLength(-6, "一行のデータ数が異常です"),
	IllegalLineNumber(-7, "データの行数が異常です"),
	BetweenBlankLine(-8, "データ中に空行があります"),
	IllegalNumberFormat(-9, "データ中に半角数字以外の文字が含まれています"),
	
	IOException(-98, "ファイル操作で予期せぬエラーが発生しました"),
	UnsupportWidth(-99, "9×9以外はサポート外です"),
	;
	
	private int errorCode;
	private String message;
	private NumberPlaceState(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getMessage() {
		return message;
	}
	
}
