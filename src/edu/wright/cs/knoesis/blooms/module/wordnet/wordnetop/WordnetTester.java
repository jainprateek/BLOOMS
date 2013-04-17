// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WordnetTester.java

package edu.wright.cs.knoesis.blooms.module.wordnet.wordnetop;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class WordnetTester
{

    public WordnetTester()
    {
    }

    public static void main(String args[])
    {
        WordnetTester wordnetTester = new WordnetTester();
        List hypernymList = wordnetTester.getHypernym("fauna");
        for(int i = 0; i < hypernymList.size(); i++)
        {
            IWord word = (IWord)hypernymList.get(i);
            System.out.println((new StringBuilder("Hypernym is:")).append(word.getLemma().toString()).toString());
        }

        wordnetTester.getHypernym(hypernymList);
    }

    public List getHypernym(String phrase)
    {
        List hypernymsList = new ArrayList();
        if(dict == null)
        {
            System.out.println("Dictionary is null");
            System.exit(0);
        }
        IIndexWord idxWord = dict.getIndexWord(phrase, POS.NOUN);
        if(idxWord != null)
        {
            IWordID wordID = (IWordID)idxWord.getWordIDs().get(0);
            IWord word = dict.getWord(wordID);
            ISynset synset = word.getSynset();
            List hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
            List words;
            for(Iterator iterator = hypernyms.iterator(); iterator.hasNext(); hypernymsList.addAll(words))
            {
                ISynsetID sid = (ISynsetID)iterator.next();
                words = dict.getSynset(sid).getWords();
            }

            hypernymsList = keepUniqueTerms(hypernymsList);
        }
        return hypernymsList;
    }

    public List getSynonyms(String phrase)
    {
        List synonymsList = new ArrayList();
        if(dict == null)
        {
            System.out.println("Dictionary is null");
            System.exit(0);
        }
        IIndexWord idxWord = dict.getIndexWord(phrase, POS.NOUN);
        if(idxWord != null)
        {
            IWordID wordID = (IWordID)idxWord.getWordIDs().get(0);
            IWord word = dict.getWord(wordID);
            ISynset synset = word.getSynset();
            List words = new ArrayList();
            IWord w;
            for(Iterator iterator = synset.getWords().iterator(); iterator.hasNext(); words.add(w))
                w = (IWord)iterator.next();

            synonymsList = keepUniqueTerms(words);
        }
        return synonymsList;
    }

    List keepUniqueTerms(List hypernymsList)
    {
        ArrayList uniqueList = new ArrayList();
        ArrayList uniqueHypernymList = new ArrayList();
        for(int i = 0; i < hypernymsList.size(); i++)
        {
            IWord word = (IWord)hypernymsList.get(i);
            if(!uniqueList.contains(word.getLemma().toString()))
            {
                uniqueList.add(word.getLemma().toString());
                uniqueHypernymList.add(word);
            }
        }

        return uniqueHypernymList;
    }

    public void getSynonym(List words)
    {
        for(int i = 0; i < words.size(); i++)
        {
            IWord word = (IWord)words.get(i);
            System.out.println((new StringBuilder("\n\nFor Word:")).append(word.getLemma()).toString());
            List hypernymList = getHypernym(word.getLemma());
            for(int j = 0; j < hypernymList.size(); j++)
            {
                IWord hypernym = (IWord)hypernymList.get(j);
                System.out.println((new StringBuilder("Hypernym:")).append(hypernym.getLemma()).toString());
            }

        }

    }

    public void getHypernym(List words)
    {
        for(int i = 0; i < words.size(); i++)
        {
            IWord word = (IWord)words.get(i);
            System.out.println((new StringBuilder("\n\nFor Word:")).append(word.getLemma()).toString());
            List hypernymList = getHypernym(word.getLemma());
            for(int j = 0; j < hypernymList.size(); j++)
            {
                IWord hypernym = (IWord)hypernymList.get(j);
                System.out.println((new StringBuilder("Hypernym:")).append(hypernym.getLemma()).toString());
            }

        }

    }

    public IWord getIWordForPhrase(String phrase)
    {
        IWord word = null;
        IIndexWord idxWord = dict.getIndexWord(phrase, POS.NOUN);
        if(idxWord != null)
        {
            IWordID wordID = (IWordID)idxWord.getWordIDs().get(0);
            word = dict.getWord(wordID);
        }
        return word;
    }

    public boolean areSynoynyms(String word1, String word2)
    {
        boolean synonyms = false;
        List synoymList = getSynonyms(word1);
        List synoym1List = getSynonyms(word2);
        ArrayList word1List = new ArrayList();
        ArrayList word2List = new ArrayList();
        for(int i = 0; i < synoymList.size(); i++)
        {
            IWord word = (IWord)synoymList.get(i);
            word1List.add(word.getLemma());
        }

        for(int i = 0; i < synoym1List.size(); i++)
        {
            IWord word = (IWord)synoym1List.get(i);
            word2List.add(word.getLemma());
        }

        word1List.retainAll(word2List);
        if(word1List.size() > 0)
            synonyms = true;
        return synonyms;
    }

    static String getWNLocation()
    {
        String str = "";
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("environment.txt"));
            str = in.readLine();
            in.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    static IDictionary dict;

    static 
    {
        dict = null;
        String wnhome = System.getenv("WNHOME");
        String path = (new StringBuilder(String.valueOf(wnhome))).append(File.separator).append("dict").toString();
        System.out.println((new StringBuilder("Path to dictionary:")).append(getWNLocation()).toString());
        URL url = null;
        try
        {
            File f = new File((new StringBuilder(String.valueOf(getWNLocation()))).append(File.separator).append("dict").toString());
            URI uri = f.toURI();
            url = uri.toURL();
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        dict = new Dictionary(url);
        dict.open();
    }
}
