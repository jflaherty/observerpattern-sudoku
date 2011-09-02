import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.List;

public class Cell extends Observable implements Observer {

	private List<Integer> values = new ArrayList<Integer>();
	private boolean isSolved = false;
	private int row;
	private int col;

	// Add all possible entries (1 to 9) in each cell
	public Cell(int row, int col) {

		this.row = row;
		this.col = col;
		for (int n = 1; n <= 9; n++) {
			values.add(new Integer(n));
		}
	}

	// add cells that are in the same line or same box as observers
	public synchronized void addObserver(Cell[][] cells) {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boolean isSame = (i == row) && (j == col);
				boolean isSameLine = (i == row) || (j == col);
				boolean isSecondary = (i / 3 == row / 3) && (j / 3 == col / 3);
				if (!isSame && (isSameLine || isSecondary)) {
					super.addObserver(cells[i][j]);
				}
			}
		}
	}

	// add the known value after clearing and notify observers
	public void setValue(int value) {

		values.clear();
		values.add(new Integer(value));
		isSolved = true;
		super.setChanged();
		super.notifyObservers(new Integer(value));
	}

	// Observe and remove the entry set in the observable
	public void update(Observable o, Object arg) {

		values.remove(arg);
		if (!isSolved && values.size() == 1) {
			Integer value = (Integer) values.get(0);
			setValue(value.intValue());
		}
	}

	// A cell is solved if the it has just one value
	public int getValue() {

		if (values.size() == 1) {
			return ((Integer) values.get(0)).intValue();
		}

		return 0;
	}

}