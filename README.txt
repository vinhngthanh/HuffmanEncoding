Name: Vinh Nguyen
ID: 659824967

Compilation:
- To encode a file: java HuffmanSubmit enc filename
- To decode a file: java HuffmanSubmit dec

Explaination of code:
#Encode:
First, I read the inputfile and convert all the string into a single char array. 
Next, I create a frequency array to track the frequency of each character. Next, I 
build a Trie from the freq[] array. In the buildTrie method I create a priority 
queue which add each character once with its frequency number into the priority
queue. After adding all of them, I poll 2 smallest frequency out and create a parent
node for them and add the parent back to the queue. Next, I build the code table to
look up the characters in the trie. If it moves left then add 0, moves right add 1 
and if it is leaf, print the whole thing. Next, I write the Trie out for decode later
through the writeTrie method. This prints leaf from left to right so we can build the
tree again later. Next, I print the frequency out to a file called "freq.txt". Next,
I print out the length of bits if original string to "length.txt". Finally, use the
code table to print out the encoded file to the "enc.txt".

#Decode:
First, I read the "trie.txt" produced in encoding process then build the trie back
recursively. Next, I read the "length.txt" file to determine the characters needed 
to be read. Then, I move according to the encoded file, move right if encounter 1,
move left if 0. If reach the leaf, I print it out. 