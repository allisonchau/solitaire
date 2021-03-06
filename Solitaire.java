package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		int target=27;
		CardNode finder=deckRear.next;
		CardNode prev=deckRear;
		do{
			if(finder.cardValue!=target){
				prev=finder;
				finder=finder.next;
				
			}
			else{
				int temp=finder.next.cardValue;
				finder.cardValue=temp;
				finder.next.cardValue=target;
				prev=finder.next;
				finder=finder.next.next;
			}
		}while(prev!=deckRear);
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD
		int target=28;
		CardNode finder=deckRear.next, prev=deckRear;
		do{
			if(finder.cardValue!=target){
				prev=finder;
				finder=finder.next;
			}
			else{
				int temp1=finder.next.cardValue;
				int temp2=finder.next.next.cardValue;
				finder.cardValue=temp1;
				finder.next.cardValue=temp2;
				finder.next.next.cardValue=target;
				prev=finder.next.next;
				finder=finder.next.next.next;
			}
		}while(prev!=deckRear);
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		int t1=27,t2=28;
		CardNode prev=deckRear,curr=deckRear.next;
		CardNode head1=deckRear.next,head2=null;
		CardNode tail1=null,tail2=deckRear,target1=null,target2=null;
		do{
			if(curr.cardValue!=t1&&curr.cardValue!=t2){
				prev=curr;
				curr=curr.next;
			}
			else{
				if(tail1==null){
					tail1=prev;
					target1=curr;
					prev=curr;
					curr=curr.next;
				}
				else{
					target2=curr;
					head2=curr.next;
					prev=curr;
					curr=curr.next;
				}
			}
		}while(prev!=deckRear);
		if(deckRear.next==target1){
			deckRear=target2;
		}
		else if(deckRear==target2){
			deckRear=tail1;
		}
		else{
			deckRear=tail1;
			tail1.next=head2;
			target2.next=head1;
			tail2.next=target1;
		}
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		// COMPLETE THIS METHOD
		int count=deckRear.cardValue;
		CardNode a=deckRear.next,b,c,d;
		CardNode prev=deckRear,curr=deckRear.next;
		for(int i=0;i<count;i++){
			prev=curr;
			curr=curr.next;
		}
		d=prev;
		b=curr;
		while(curr!=deckRear){
			prev=curr;
			curr=curr.next;
		}
		c=prev;
		if(count==27||count==28){
			return;
		}
		else{
			d.next=deckRear;
			deckRear.next=b;
			c.next=a;
		}		
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		int countVal=deckRear.next.cardValue;
		int key;
		if(countVal==27||countVal==28){
			key=deckRear.cardValue;
		}
		else{
			CardNode prev=deckRear,curr=deckRear.next;
			for(int i=0;i<countVal;i++){
				prev=curr;
				curr=curr.next;
			}
			key=curr.cardValue;
		}
		return key;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
		String encrypt="";
		for(int i=0;i<message.length();i++){
			char x=message.charAt(i);
			if(Character.isLetter(x)){
				int pos=x-'A'+1;
				this.jokerA();
				this.jokerB();
				this.tripleCut();
				this.countCut();
				int key=this.getKey();
				int sum=key+pos>26 ? (key+pos)-26 : key+pos;
				char y=(char)(sum-1+'A');
				encrypt=encrypt+y;
			}
		}
	    return encrypt;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
	    String decrypt="";
	    for(int i=0;i<message.length();i++){
	    	char a=message.charAt(i);
	    	int code=a-'A'+1;
	    	this.jokerA();
	    	this.jokerB();
	    	this.tripleCut();
	    	this.countCut();
	    	int key=this.getKey();
	    	int diff=code>key ? code-key : (code+26)-key;
	    	char b=(char)(diff-1+'A');
	    	decrypt=decrypt+b;
	    }
	    return decrypt;
	}
}
