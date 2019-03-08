package ur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;





/**
 * Class that represent a Defense quadrant
 * A quadrant has a uniq id, and a DefenseStatus
 * 
 *
 */
@Entity
@Table(name = "Defense")
@XmlRootElement()
public class Defense {
	@Id
	@GeneratedValue
	private int id;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private DefenseStatus status;
	
	public Defense() {
		this.status = DefenseStatus.Empty;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public DefenseStatus getStatus() {
		return status;
	}


	public void setStatus(DefenseStatus status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Defense [id=" + id + ", status=" + status + "]";
	}

}
