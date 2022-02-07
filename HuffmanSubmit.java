//Vinh Nguyen
//ID: 659824967

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;


public class HuffmanSubmit {
	
    private static final int R = 256;

    private HuffmanSubmit() { }

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }        
    }
	
    public static void encode(String inputFile, String outputFile, String freqFile) throws IOException {
    	BinaryIn inEnc = new BinaryIn(inputFile);
        String s = inEnc.readString();
        char[] input = s.toCharArray();
        
        int[] freq = new int[R];      
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        
        Node root = buildTrie(freq);

        String[] st = new String[R];
        buildCode(st, root, "");
        
        BinaryOut outTrie = new BinaryOut("trie.txt");
        writeTrie(root, outTrie);
        outTrie.close();
        
        FileWriter fileWriter = new FileWriter(freqFile);
    	PrintWriter printWriter = new PrintWriter(fileWriter);
    	
        for(int i = 0; i < R; i++) {
        	if(freq[i] != 0) {
        		String acsii = Integer.toBinaryString(i);
        		int zero = 8 - acsii.length();
        		for(int j = 0; j < zero; j++) {
        			acsii = acsii + "0";
        		}
            	printWriter.println(acsii + " : " + freq[i]);    		
        	}
        }
        printWriter.close();
        
        BinaryOut outLength = new BinaryOut("length.txt");
        outLength.write(input.length);
        outLength.close();
        
    	BinaryOut outEnc = new BinaryOut(outputFile);

        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                	outEnc.write(false);
                }
                else if (code.charAt(j) == '1') {
                	outEnc.write(true);
                }
                else throw new IllegalStateException("Illegal state");
            }
        }

        outEnc.close();
    }

    private static void writeTrie(Node x, BinaryOut outTrie) {
        if (x.isLeaf()) {
        	outTrie.write(true);
        	outTrie.write(x.ch, 8);
            return;
        }
        outTrie.write(false);
        writeTrie(x.left,outTrie);
        writeTrie(x.right,outTrie);       
    }

    private static Node buildTrie(int[] freq) {

        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        for (char c = 0; c < R; c++)
            if (freq[c] > 0)
                pq.add(new Node(c, freq[c], null, null));

        while (pq.size() > 1) {
            Node left  = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return pq.poll();
    }

    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        }
    }

    public static void decode(String inputFile, String outputFile, String freqFile) {
    	BinaryIn inTrie = new BinaryIn("trie.txt");
        Node root = readTrie(inTrie); 
        
    	BinaryIn inLength = new BinaryIn("length.txt");
        int length = inLength.readInt();

    	BinaryIn inDec = new BinaryIn(inputFile);
        BinaryOut outDec = new BinaryOut(outputFile);
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = inDec.readBoolean();
                if (bit) x = x.right;
                else     x = x.left;
            }
            outDec.write(x.ch, 8);
        }
        outDec.close();
    }

    private static Node readTrie(BinaryIn inTrie) {
        boolean isLeaf = inTrie.readBoolean();
        if (isLeaf) {
            return new Node(inTrie.readChar(), -1, null, null);
        }
        else {
            return new Node('\0', -1, readTrie(inTrie), readTrie(inTrie));
        }
    }

    public static void main(String[] args) throws IOException {
        if      (args[0].equals("enc")) encode(args[1], "enc.txt", "freq.txt");
        else if (args[0].equals("dec")) decode("enc.txt", "dec.txt", "freq.txt");
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
