package number.strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import number.NumberPlaceAnsableResult;
import number.NumberPlaceCell;
import number.NumberPlaceResult;
import number.NumberPlaceState;
import number.NumberPlaceTable;

public class SimpleAnalyzer implements NumberPlaceStrategy<List<NumberPlaceTable>> {

	private NumberPlaceTable numberTable;
	private NumberPlaceResult result;
	
	public SimpleAnalyzer(NumberPlaceTable numberTable) {
		this.numberTable = numberTable;
		this.result = new NumberPlaceResult();
		this.result.setState(NumberPlaceState.NoAnser);
	}
	
	@Override
	public NumberPlaceAnsableResult<List<NumberPlaceTable>> calculate() {
		innerCalculate();
		return result;
	}

	private void innerCalculate() {
//		System.out.println("innerCalculate");
		// ���̓f�[�^�ɕs�����Ȃ����`�F�b�N���s��
		if (checkError()) {
			result.setState(NumberPlaceState.NoAnser);
			return;
		}
		
		// �m�肵�Ă��鐔�������ׂĖ��߂�
		updateCells();
		
		// �������Ă��邩�m�F
		if (!checkFinish()) {
			// ���l�����肵�ĉ������߂�(�ċA����)
			if (!postulateNumber()) {
				result.setState(NumberPlaceState.NoAnser);
				return;
			}
		} else {
			result.addResult(numberTable);
			result.setState(NumberPlaceState.OneAnser);
		}
	}
	
	/**
	 * �S�Z���̍X�V����
	 * 1.�e�Z���̓��͉\�������X�V
	 * 2.�e�Z���̓��͉\������1�����̃Z���𖄂߂�
	 * 3.�e�Z����������u���b�N�E�s�E��ɓ��͂���Ă��鐔������Z���̓��͉\����������o���Ė��߂�
	 * 4.1�`3�������Ȃ�܂ōs��
	 */
	private void updateCells() {
		boolean update = true;
		while(update) {
			update = false;
			checkEnableNumber();
			update = (update || searchInputOnlyValue()); // �u���b�N�E��E�s�ŁA1�������܂��Ă��Ȃ�������T���Ė��߂�
			update = (update || searchCanInputOnlyValue()); // �Z���ɔz�u�ł��鐔�����m�肵�Ă���ꏊ��T���Ė��߂�
		}
	}
	
	/**
	 * �e�Z���ɓ��͉\������1�����z�u���ĉ������߂�(�ċA����)
	 * ���z�u�������ʁA�G���[(����)�������������_�ŁA���z�u�܂��̏�Ԃɖ߂��āA�ʂ̓��͉\���������z�u����
	 * ���ׂẲ������߂邽�߁A����1�łĂ��������~�߂Ȃ�
	 * @return ����
	 */
	private boolean postulateNumber() {
//		System.out.println("postulateNumber");
		boolean bResult = false;
		// ������o�b�N�A�b�v
		NumberPlaceTable backup = new NumberPlaceTable(numberTable);
		NumberPlaceCell blankCell = numberTable.getHiPriorityCell();
		int row = blankCell.row();
		int clumn = blankCell.clumn();
		List<Integer> enableNumberList = blankCell.enableNumberList();
		
		// �z�u�ł��鐔�������ꂼ�ꎎ��
		for (Integer number : enableNumberList) {
			System.out.println("["+row+","+clumn+"] �� "+number+" �����z�u���܂�");
			numberTable.getCell(row, clumn).setNumber(number);
			
			updateCells();
			
			if (checkError()) {
				System.out.println("�G���[�̂��߃��[���o�b�N���܂�");
				numberTable = backup;
			} else {
				if (checkFinish()) {
					System.out.println("�𓚂�������܂���");
					result.addResult(numberTable);
					if (result.output().size() > 1) {
						result.setState(NumberPlaceState.ManyAnser);
					} else if (result.output().size() == 1) {
						result.setState(NumberPlaceState.OneAnser);
					}
					bResult = true;
				} else {
					if (postulateNumber()) {
						bResult = true;
					} else {
						System.out.println("�G���[�̂��߃��[���o�b�N���܂�");
						numberTable = backup;
					}
				}
			}
		}
		return bResult;
	}
	
	private boolean checkError() {
//		System.out.println("checkError");
		// �������͒l��10�����̓G���[�ɂ���(�����Ȃ��炵��)
		int numCount = 0;
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isNotBlank()) {
				numCount++;
			}
		}
		if (numCount < 10) {
			System.out.println("�G���[: �������͒l�͍Œ�ł�10���͂��Ă�������");
			return true;
		}
		
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isNotBlank()) {
				// �������Ő��l�d���`�F�b�N
				if (numberTable.existSameNumberAtRow(cell, cell.row())) {
					System.out.println(cell.row() + "�s�ŏd���G���[");
					return true;
				}
				
				// �c�����Ő��l�d���`�F�b�N
				if (numberTable.existSameNumberAtClumn(cell, cell.clumn())) {
					System.out.println(cell.clumn() + "��ŏd���G���[");
					return true;
				}
				
				// �u���b�N���Ő��l�d���`�F�b�N
				if (numberTable.getBlock(cell).existSameNumber(cell)) {
					System.out.println("{"+numberTable.getBlockRow(cell)+","+numberTable.getBlockClumn(cell)+"}�u���b�N���̏d���G���[");
					return true;
				}
			}
		}
		
		// �����͂����͉\�Ȑ��l���Ȃ��Z���̃`�F�b�N
		if (numberTable.existContradictCell()) {
			System.out.println("�����̓Z���ɑ΂��ē��͉\�Ȑ��l���Ȃ������G���[");
		}
		
		return false;
	}
	
	private boolean checkFinish() {
//		System.out.println("checkFinish");
		return !numberTable.existBlankCell();
	}
	
	private void checkEnableNumber() {
//		System.out.println("checkEnableNumber");
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isBlank()) {
				// �Z����������u���b�N�����p���Ă��鐔������Z���̓��͉\�Ȑ������i�荞��
				cell.disableNumber(numberTable.getBlock(cell).numberSet(cell));
				
				// �������ŗ��p���Ă��鐔������Z���̓��͉\�Ȑ������i�荞��
				cell.disableNumber(numberTable.numberSetRow(cell));
				
				// �c�����ŗ��p���Ă��鐔������Z���̓��͉\�Ȑ������i�荞��
				cell.disableNumber(numberTable.numberSetClumn(cell));
			}
		}
	}
	
	private boolean searchInputOnlyValue() {
//		System.out.println("searchInputOnlyValue");
		boolean bResult = false;
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isBlank()) {
				if (cell.getEnableNumberCount() == 1) {
					int number = cell.enableNumberList().get(0);
					System.out.println("["+cell.row()+","+cell.clumn()+"] �� "+number+" ��z�u(OnlyValue)");
					numberTable.setNumber(cell.row(), cell.clumn(), number);
					bResult = true;
				}
			}
		}
		return bResult;
	}
	
	private boolean searchCanInputOnlyValue() {
//		System.out.println("searchCanInputOnlyValue");
		boolean bResult = false;
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isBlank()) {
				// �������ɓ��͂���Ă��鐔�����W�v
				Set<Integer> inputNumbers = new HashSet<Integer>();
				inputNumbers.addAll(numberTable.numberSetRow(cell));
				
				// �c�����ɓ��͂���Ă��鐔�����W�v
				inputNumbers.addAll(numberTable.numberSetClumn(cell));
				
				// �u���b�N���œ��͂���Ă��鐔�����W�v
				inputNumbers.addAll(numberTable.getBlock(cell).numberSet(cell));
				
				// ���͂���Ă��鐔�������ׂĖ����ɂ���
				cell.disableNumber(inputNumbers);
				
				// ���͉\�Ȑ�����1�̏ꍇ
				if (cell.canInputOnlyValue()) {
					int number = cell.enableNumberList().get(0);
					System.out.println("["+cell.row()+","+cell.clumn()+"] �� "+number+" ��z�u(CanOnlyValue)");
					cell.setNumber(number);
					bResult = true;
				}
			}
		}
		return bResult;
	}
}
