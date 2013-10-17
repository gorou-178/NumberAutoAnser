package number;

public enum NumberPlaceState {

	OneAnser(1, "1�𓚂�����܂���"),
	ManyAnser(2, "2�ȏ�̉𓚂�����܂���"),
	
	NoAnser(99, "�𓚂�����܂���"),
	
	NoExistFile(-1, "�t�@�C�������݂��܂���"),
	CantReadFile(-2, "�t�@�C�������݂��܂���"),
	NoFile(-3, "�t�@�C�������݂��܂���"),
	NoExtension(-4, "�t�@�C�������݂��܂���"),
	OtherExtension(-5, "�e�L�X�g�t�@�C��(.txt)���w�肵�Ă�������"),
	IllegalLineLength(-6, "��s�̃f�[�^�����ُ�ł�"),
	IllegalLineNumber(-7, "�f�[�^�̍s�����ُ�ł�"),
	BetweenBlankLine(-8, "�f�[�^���ɋ�s������܂�"),
	IllegalNumberFormat(-9, "�f�[�^���ɔ��p�����ȊO�̕������܂܂�Ă��܂�"),
	
	IOException(-98, "�t�@�C������ŗ\�����ʃG���[���������܂���"),
	UnsupportWidth(-99, "9�~9�ȊO�̓T�|�[�g�O�ł�"),
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
