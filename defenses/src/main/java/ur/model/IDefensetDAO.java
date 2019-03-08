package ur.model;

import java.util.List;

public interface IDefensetDAO {
	
	
	public List<Defense> list();
	public Defense getDefense(int id);
	public void persistDefense(Defense defense);
	public void deleteDefense(Defense defense);

}
