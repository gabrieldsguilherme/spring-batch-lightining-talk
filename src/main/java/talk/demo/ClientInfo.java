package talk.demo;

public class ClientInfo {
	
	private String document;
	
	private String name;
	
	private Integer age;

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "Client -> document: " + document + ", name: " + name + ", age: " + age;
	}
	
}
