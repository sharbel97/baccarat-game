import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Objects;

class MyTest {
	BaccaratDealer myBaccaratDealer = new BaccaratDealer();
	BaccaratGameLogic myBaccaratGameLogic = new BaccaratGameLogic();

	@BeforeAll
	static void setUp() {


	}

	@Test
	void BaccaratInfoConstrucorTest() {
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Testing");
		assertEquals(myBaccaratInfo.message, "Testing", "failed constructor");
	}

	@Test
	void BaccaratInfoConstrucorTest2() {
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		assertEquals(myBaccaratInfo.betOn, "Player", "failed constructor");
		assertEquals(myBaccaratInfo.bet, 5.0, "failed constructor");
	}

	@Test
	void BaccaratDealerDeckSize() {
		BaccaratDealer myBaccaratDealer = new BaccaratDealer();
		myBaccaratDealer.generateDeck();
		assertEquals(myBaccaratDealer.deckSize(), 52, "wrong deck size");
	}

	@Test
	void BaccaratDealerGenerateDeckTest() {
		// generate deck
		myBaccaratDealer.generateDeck();
		Card tempCard = myBaccaratDealer.deck.get(51);
		Card tempCard2 = myBaccaratDealer.deck.get(0);
		Card tempCard3 = myBaccaratDealer.deck.get(15);
		assertEquals(tempCard.suite, "diamonds", "wrong suite");
		assertEquals(tempCard.value, 13, "wrong value");
		assertEquals(tempCard2.suite, "clubs", "wrong suite");
		assertEquals(tempCard2.value, 1, "wrong value");
		assertEquals(tempCard3.suite, "diamonds", "wrong suite");
		assertEquals(tempCard3.value, 4, "wrong value");

		// add draw hand AND draw one test cases!!!
	}

	@Test
	void BaccaratDealerShuffleDeckTest() {
		myBaccaratDealer.generateDeck();
		Card tempCard = myBaccaratDealer.deck.get(51);
		assertEquals(tempCard.suite, "diamonds", "wrong suite");
		assertEquals(tempCard.value, 13, "wrong value");
		while (Objects.equals(myBaccaratDealer.deck.get(51).suite, "diamonds")) { // without shuffle is an infinite loop
			myBaccaratDealer.shuffleDeck();
		}
	}

	@Test
	void BaccaratGameLogicEvaluateBankerDrawTest() {
		ArrayList<Card> playerList = new ArrayList<>();
		playerList.add(new Card(0,5));
		playerList.add(new Card(0, 2));

		ArrayList<Card> playerList2 = new ArrayList<>();
		playerList2.add(new Card(0,3));
		playerList2.add(new Card(0, 2));
		assertFalse(myBaccaratGameLogic.evaluateBankerDraw(playerList, new Card(0,0)));
		assertFalse(myBaccaratGameLogic.evaluateBankerDraw(playerList, new Card(0,0)));
		assertTrue(myBaccaratGameLogic.evaluateBankerDraw(playerList2, new Card(0,4)));
		assertTrue(myBaccaratGameLogic.evaluateBankerDraw(playerList2, new Card(0,5)));

	}

	@Test
	void BaccaratGameLogicEvaluateBankerDrawNullTest() {
		ArrayList<Card> playerList = new ArrayList<>();
		playerList.add(new Card(0,5));
		playerList.add(new Card(0, 1));

		assertFalse(myBaccaratGameLogic.evaluateBankerDraw(playerList, null));
	}

	@Test
	void BaccaratGameLogicEvaluatePlayerDraw() {
		ArrayList<Card> playerList = new ArrayList<>();
		playerList.add(new Card(0,2));
		playerList.add(new Card(0, 2));

		assertTrue(myBaccaratGameLogic.evaluatePlayerDraw(playerList));
	}

	@Test
	void BaccaratGameLogicWhoWonTest() {
		ArrayList<Card> playerList = new ArrayList<>();
		ArrayList<Card> bankerList = new ArrayList<>();
		playerList.add(new Card(1, 2));
		playerList.add(new Card(1, 3));
		bankerList.add(new Card(1, 5));
		bankerList.add(new Card(1, 13));
		assertEquals(myBaccaratGameLogic.whoWon(playerList,bankerList), "Tie", "Not a Tie");

		playerList.add(new Card(1, 1));
		assertEquals(myBaccaratGameLogic.whoWon(playerList,bankerList), "Player", "Not Player");

		bankerList.add(new Card(1, 2));
		assertEquals(myBaccaratGameLogic.whoWon(playerList,bankerList), "Banker", "Not Banker");

	}

	@Test
	void BaccaratGameConstructorTest() {
		BaccaratGame myBaccaratGame = new BaccaratGame("Player", 5.0);
		assertEquals(myBaccaratGame.betOn, "Player", "Wrong constructor");
		assertEquals(myBaccaratGame.currentBet, 5.0, "Wrong constructor");
	}

	@Test
	void BaccaratGameEvaluateWinningsTie() {
		BaccaratGame myBaccaratGame = new BaccaratGame("Tie", 5.0);
		ArrayList<Card> myList = new ArrayList<>();
		ArrayList<Card> myList2 = new ArrayList<>();
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 2));
		myList2.add(new Card(1, 1));
		myList2.add(new Card(1, 2));
		myBaccaratGame.playerHand = myList;
		myBaccaratGame.bankerHand = myList2;
		assertEquals(myBaccaratGame.evaluateWinnings(), 40.0, "Eval wins tie wrong");
	}

	@Test
	void BaccaratGameEvaluateWinningsLoss() {
		BaccaratGame myBaccaratGame = new BaccaratGame("Tie", 5.0);
		ArrayList<Card> myList = new ArrayList<>();
		ArrayList<Card> myList2 = new ArrayList<>();
		myList.add(new Card(1, 2));
		myList.add(new Card(1, 2));
		myList2.add(new Card(1, 1));
		myList2.add(new Card(1, 2));
		myBaccaratGame.playerHand = myList;
		myBaccaratGame.bankerHand = myList2;
		assertEquals(myBaccaratGame.evaluateWinnings(), -5.0, "Eval wins loss wrong");
	}

	@Test
	void BaccaratGameLogicHandTotalTest() {
		ArrayList<Card> myList = new ArrayList<>();
		assertEquals(myBaccaratGameLogic.handTotal(myList), 0, "Not 0");
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 8));
		assertEquals(myBaccaratGameLogic.handTotal(myList), 9, "Not 9");

	}

	@Test
	void BaccaratGameLogicHandTotalTest2() {
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 8));
		myList.add(new Card(1, 11));
		myList.add(new Card(1, 12));
		myList.add(new Card(1, 13));
		myList.add(new Card(1, 5));
		assertEquals(myBaccaratGameLogic.handTotal(myList), 4, "Not 4");
	}

	@Test
	void playGameNaturalWinTest() {
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 5));
		myList.add(new Card(1, 4));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 5));
		myList2.add(new Card(1, 4));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Banker", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Banker", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.payout, -5, "Not -5");
		assertTrue(myBaccaratInfo.naturalWin, "Not Natty Win");
	}

	@Test
	void playGameNaturalWinTest2() {
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 5));
		myList.add(new Card(1, 4));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 5));
		myList2.add(new Card(1, 4));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Tie", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Tie", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.payout, 40, "Not Tie");
		assertTrue(myBaccaratInfo.naturalWin, "Not Natty Win");

	}

	@Test
	void playGameNaturalWinTest3() {
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 11));
		myList.add(new Card(1, 12));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 5));
		myList2.add(new Card(1, 3));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.payout, -5, "Not Loss");
		assertTrue(myBaccaratInfo.naturalWin, "Not Natty Win");

	}

	@Test
	void playGameNaturalWinTest4() {
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 11));
		myList.add(new Card(1, 12));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 5));
		myList2.add(new Card(1, 3));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Banker", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Banker", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.payout, 5, "Not Loss");
		assertTrue(myBaccaratInfo.naturalWin, "Not Natty Win");

	}

	@Test
	void playGameNaturalWinTest5() {
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 1));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 2));
		myList2.add(new Card(1, 2));
		myList2.add(new Card(1,1));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Banker", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Banker", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		//assertEquals(myBaccaratInfo.payout, 5.0, "Not Loss");
		assertFalse(myBaccaratInfo.naturalWin, "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest() { // draw 3rd on player 6
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 3));
		myList.add(new Card(1, 3));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 3));
		myList2.add(new Card(1, 1));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		//assertEquals(myBaccaratInfo.payout, 5, "Not Loss");
		assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest2() { // draw 3rd on player 4
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 3));
		myList.add(new Card(1, 1));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 3));
		myList2.add(new Card(1, 1));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		//assertEquals(myBaccaratInfo.payout, 5, "Not Loss");
		assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest3() { // banker stand on 7
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 3));
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 1));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 3));
		myList2.add(new Card(1, 4));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 2, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest4() { // banker draw on 2
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 3));
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 1));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 1));
		myList2.add(new Card(1, 1));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 3, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest5() { // banker (5) draw on player null
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 3));
		myList.add(new Card(1, 4));
		//myList.add(new Card(1, 1));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 3));
		myList2.add(new Card(1, 2));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 3, "Did not Stand");
		assertEquals(myBaccaratInfo.playerHand.size(), 2, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest6() { // banker (6) stand on player null
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 3));
		myList.add(new Card(1, 4));
		//myList.add(new Card(1, 1));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 3));
		myList2.add(new Card(1, 3));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 2, "Did not Stand");
		assertEquals(myBaccaratInfo.playerHand.size(), 2, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest7() { // banker (5) stand on player 0
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 0));
		myList.add(new Card(1, 0));
		myList.add(new Card(1, 0));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 2));
		myList2.add(new Card(1, 3));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 2, "Did not Stand");
		//	assertEquals(myBaccaratInfo.playerHand.size(), 2, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest8() { // banker (2) draw on player 1
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 1));
		myList.add(new Card(1, 0));
		myList.add(new Card(1, 1));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 1));
		myList2.add(new Card(1, 1));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 3, "Did not Stand");
		//	assertEquals(myBaccaratInfo.playerHand.size(), 2, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest9() { // banker (6) draw on player 6
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 0));
		myList.add(new Card(1, 0));
		myList.add(new Card(1, 6));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 3));
		myList2.add(new Card(1, 3));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 3, "Did not Draw");
		//	assertEquals(myBaccaratInfo.playerHand.size(), 2, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

	@Test
	void playGameThirdDrawTest10() { // banker (7) stand on player 7
		ArrayList<Card> myList = new ArrayList<>();
		myList.add(new Card(1, 0));
		myList.add(new Card(1, 0));
		myList.add(new Card(1, 7));
		ArrayList<Card> myList2 = new ArrayList<>();
		myList2.add(new Card(1, 3));
		myList2.add(new Card(1, 4));
		//myList2.add(new Card(1, 2));
		BaccaratInfo myBaccaratInfo = new BaccaratInfo("Player", 5.0);
		BaccaratGame myBaccaratGame2 = new BaccaratGame("Player", 5.0);
		myBaccaratGame2.playerHand = myList;
		myBaccaratGame2.bankerHand = myList2;
		myBaccaratGameLogic.playGame(myBaccaratInfo, myBaccaratGame2);
		assertEquals(myBaccaratInfo.bankerHand.size(), 2, "Did not Stand");
		//	assertEquals(myBaccaratInfo.playerHand.size(), 2, "Did not Stand");
		//assertNotNull(myBaccaratInfo.playerHand.get(2), "Not Natty Win");

	}

}



