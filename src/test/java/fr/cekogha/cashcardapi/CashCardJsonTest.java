package fr.cekogha.cashcardapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import fr.cekogha.cashcardapi.model.CashCard;

@JsonTest
public class CashCardJsonTest {

	@Autowired
	private JacksonTester<CashCard> json;

	@Autowired
	private JacksonTester<CashCard[]> jsonList;
	
	private CashCard[] cashCards;
	
	@BeforeEach
	void setUp() {
        cashCards = Arrays.array(
                new CashCard(99L, 123.45,"christian"),
                new CashCard(100L, 1.00,"christian"),
                new CashCard(101L, 150.00,"christian"));
	}
	
	@Test
	public void test() {
		assertThat(42).isEqualTo(42);
	}
	
	@Test
	public void serializationTest() throws IOException {
		CashCard card = cashCards[0];
		assertThat(json.write(card)).isStrictlyEqualToJson("single.json");
		assertThat(json.write(card)).hasJsonPathNumberValue("@.id");
		assertThat(json.write(card)).extractingJsonPathNumberValue("@.id")
		.isEqualTo(99);
		assertThat(json.write(card)).hasJsonPathNumberValue("@.amount");
		assertThat(json.write(card)).extractingJsonPathNumberValue("@.amount")
		.isEqualTo(123.45);
	}
	
	@Test
	public void deserializationTest() throws IOException {
		String expected = """
				{
					"id":99,
					"amount":123.45,
					"owner":"christian"
				}
				""";
		assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45,"christian"));
		assertThat(json.parseObject(expected).id()).isEqualTo(99);
		assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
	}
	
	@Test
	public void serializationListTest() throws IOException {
		assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");		
	}

	@Test
	public void deserializationListTest() throws IOException {
		String expected = """
				[
					{
						"id":99,
						"amount":123.45,
						"owner":"christian"
					},
					{
						"id":100,
						"amount":1.00,
						"owner":"christian"
					},
					{
						"id":101,
						"amount":150.00,
						"owner":"christian"
					}
				]
				""";
		assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
	}
}
