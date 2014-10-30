package spiritualjournal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/******************************************************************************
 * @author mormon
 *****************************************************************************/
public class FileService {
    
    /**************************************************************************
     * READ XML
     * This will read in the XML file and grab all of the information necessary.
     * Also, the processData function will be called in order to set the date
     * up accordingly.
     * @param file
     * @return
     * @throws InterruptedException 
     *************************************************************************/
    public List<Entry> readXML(String file) throws InterruptedException {
        List<Entry> entryList = new ArrayList<>();
        
        System.out.println("Loading file: \"" + file + "\"\n");
        Thread.sleep(1000);
        try {
            File xmlFile = new File(file);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            
            doc.getDocumentElement().normalize();
            entryList = processData(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entryList;
    }
    
    /*************************************************************************
     * GET PROPERTIES
     * This grabs all of the information from the properties file.
     * @param value
     * @param file
     * @return
     * @throws Exception 
     ************************************************************************/
    public String getProperties(String value, String file) throws Exception {
        Properties prop = new Properties();
        String propVal = null;
        
        try {
            prop.load(getClass().getResourceAsStream(file));
            propVal = prop.getProperty(value);
        }
        catch (Exception ex) {
            System.out.println("Could not load properties file " + file);
            //ex.printStackTrace();
        }
        
        return propVal;
    }
    
    /**************************************************************************
     * PROCESS DATA
     * This is an extension to the readXML function. It was better to separate 
     * the process of the file in order keep it clean. It will allow the XML 
     * file to be read in by grabbing the information from each tag.
     * @param doc
     * @return 
     *************************************************************************/
    public List<Entry> processData(Document doc) {
        List<Entry> entryList = new ArrayList<>();
        List<String> scriptures = new ArrayList<>();
        List<String> topic = new ArrayList<>();
        String date;
        String content = null;
        String scrip;
            
        NodeList entry = doc.getElementsByTagName("entry");
            
        for (int temp = 0; temp < entry.getLength(); ++temp) {
            Node node = entry.item(temp);
                
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList childNodes = node.getChildNodes();
                    
                for (int i = 0; i < childNodes.getLength(); ++i) {
                    Node childNode = childNodes.item(i);
                        
                    if (childNode.getNodeName() == "scripture") {
                        Element childElement = (Element) childNode;
                            
                        scrip = childElement.getAttribute("book");
                            
                        if (childElement.hasAttribute("chapter")) {
                            scrip = scrip + " " + childElement.getAttribute("chapter");
                        }
                        if (childElement.hasAttribute("startverse")) {
                            scrip = scrip + ":" + childElement.getAttribute("startverse");
                        }
                        if (childElement.hasAttribute("endverse")) {
                            scrip = scrip + "-" + childElement.getAttribute("endverse");
                        }
                        scriptures.add(scrip);
                    }    
                    
                    if (childNode.getNodeName() == "topic") {
                        topic.add(childNode.getTextContent());
                    }
                    if (childNode.getNodeName() == "content") {
                        content = element.getTextContent().trim();
                        content = content.replaceAll("\\n\\s+", "\n");
                    }
                }
                date = element.getAttribute("date");
                entryList.add(new Entry(date, content, scriptures, topic));
            }
        } 
        return entryList;
    }
    
    /**************************************************************************
     * READ BOOK
     * A map will be created that will store the books from the file, which is 
     * stored in an array list.
     * @param file
     * @return 
     *************************************************************************/
    public Map<String, List<String>> readBook(String file) {
        Map<String, List<String>> book = new HashMap<>(); 
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            String line;
            
            while ((line = reader.readLine()) != null) {
                List<String> list = new ArrayList<>();
                String[] parts = line.split(":");
                String key = parts[0];
                
                String vList = parts[1];
                
                if (book.containsKey(key)) {
                    list = book.get(key);
                    List<String> nList = new ArrayList<>();
                    nList.add(vList);
                    list.addAll(nList);
                }
                else {
                    list.add(vList);
                }
                book.put(key, list);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }
    
    /**************************************************************************
     * READ TOPIC
     * A map will be created that will store the topics from the file, which is 
     * stored in an array list.
     * @param file
     * @return 
     *************************************************************************/
    public Map<String, List<String>> readTopic(String file) {
        Map<String, List<String>> topic = new HashMap<>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String key = parts[0];
                String vList = parts[1];
                String[] terms = vList.split(",");
                
                List<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(terms));
                topic.put(key, list);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return topic;
    }
    
    /*************************************************************************
     * BOOK WITH DATES
     * This function will create an list of entries and store them in a map 
     * which will displayed in another function.
     * @param list
     * @return 
     ************************************************************************/
    public Map<String, List<String>> bookWithDates(List<Entry> list) {
        Map<String, List<String>> mBook = new HashMap<>();
        String date;
        
        for (Entry ex : list) {
            List<String> scrip = ex.getScriptures();
            date = ex.getDate();
            
            for (String str : scrip) {
                List<String> temp = new ArrayList<>();
                
                if (mBook.containsKey(str)) {
                    temp = mBook.get(str);
                    List<String> nList = new ArrayList<>();
                    nList.add(date);
                    temp.addAll(nList);
                }
                else {
                    temp.add(date);
                }
                mBook.put(str, temp);
            }
        }
        
        return mBook;
    }
    
    /*************************************************************************
     * TOPIC WITH DATES
     * This function will create an list of the topics along with the dates 
     * and store them in a map which will displayed in another function.
     * @param list
     * @return 
     ************************************************************************/
    public Map<String, List<String>> topicWithDates(List<Entry> list) {
        Map<String, List<String>> mTopic = new HashMap<>();
        String date;
        
        for (Entry ex : list) {
            List<String> topic = ex.getTopic();
            date = ex.getDate();
            
            for (String str : topic) {
                List<String> temp = new ArrayList<>();
                
                if (mTopic.containsKey(str)) {
                    temp = mTopic.get(str);
                    List<String> nList = new ArrayList<>();
                    nList.add(date);
                    temp.addAll(nList);
                }
                else {
                    temp.add(date);
                }
                mTopic.put(str, temp);
            }
        }
        return mTopic;
    }
    
    /**************************************************************************
     * FILE PROCESS
     * This will call the makeEntry function and sort out the information and 
     * set the format of the file.
     * @param file
     * @param mScrip
     * @param mTopic
     * @return 
     *************************************************************************/
    public List<Entry> fileProcess(String file, Map<String, 
                                   List<String>> mScrip, Map<String, 
                                   List<String>> mTopic) {
        List<Entry> entry = new ArrayList<>();
        List<String> scrip = new ArrayList<>();
        List<String> topic = new ArrayList<>();
        String date = null;
        String content = "";
        String[] blocks;
         
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            
            while ((line = br.readLine()) != null) {
                content += line;
            }
            blocks = content.split("-----");
            
            for (int i = 1; i < blocks.length; i++) {
                entry.add(makeEntry(blocks[i], mScrip, mTopic));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entry;
    }
    
    /*************************************************************************
     * MAKE ENTRY
     * This function will make an entry that and grab all of the necessary 
     * information needs to make the entry.
     * @param block
     * @param mScrip
     * @param mTopic
     * @return 
     ************************************************************************/
    public Entry makeEntry(String block, Map<String, List<String>> mScrip, 
                           Map<String, List<String>> mTopic) {
        List<String> scrip = new ArrayList<>();
        List<String> topic = new ArrayList<>();
        String content = "";
        String date = null;
        
        Entry e = new Entry(date, content, scrip, topic);
        date = getDate(block);
        content = block.replace(date, "");
        e.setDate(date);
        e.setContent(content);
        
        List<String> top = findTopic(block, mTopic);
        for (String nTop: top) {
            if (!e.getTopic().contains(nTop)) {
                e.addTopic(nTop);
            }
        }
        
        List<String> scripture = findScripture(block, mScrip);
        for (String nScrip: scripture) {
            if (!e.getTopic().contains(nScrip)) {
                e.addScrip(nScrip);
            }
        }
        
        return e;
    }
    
    /**************************************************************************
     * GET DATE
     * This function will get the date in the format of "yyyy-mm-dd".
     * @param line
     * @return 
     *************************************************************************/
    public String getDate(String line) {
      String date;
      //this sets the year according to the milestone
      String dPattern = "\\d{4}-\\d{2}-\\d{2}";
      Pattern pattern = Pattern.compile(dPattern);
      Matcher match = pattern.matcher(line);
      
      if (match.find()) {
          date = match.group();
          return date;
      }
      else {
          return null;
      }  
    }
    
    /**************************************************************************
     * FIND TOPIC
     * This function will look for all of the topics in the file.
     * @param line
     * @param topics
     * @return 
     *************************************************************************/
    public List<String> findTopic(String line, Map<String, List<String>> topics) {
        List<String> topic = new ArrayList<>();
        line = line.toLowerCase();
        
        for (String key : topics.keySet()) {
            List<String> tList = topics.get(key);
            for (String top : tList) {
                top = top.toLowerCase();
                if (line.contains(key)) {
                    topic.add(key);
                }
            }
        }
        System.out.println(topic);
        return topic;
    }
    
    /**************************************************************************
     * FIND SCRIPTURE
     * This function will look for all of the scriptures that lie within the 
     * file. It will also look for different patterns that a scripture could be 
     * formatted.
     * @param line
     * @param scriptures
     * @return 
     *************************************************************************/
    public List<String> findScripture(String line, 
                                      Map<String, List<String>> scriptures) {
        List<String> scripture = new ArrayList<>();
        String tmp = "";
        
        // allow for the chapter to be recognized
        String patt1 = "((\\S+)) chapter (\\d+)";
        // allows for chapter and multiple verse
        String patt2 = "((\\S+))(\\d+):(\\d+)-(\\d+)";
        // allows for chapter and multiple verse number before book
        String patt3 = "(\\d+)((\\S+))(\\d+):(\\d+)-(\\d+)";
        // allows for number before book, chapter, and single verse
        String patt4 = "(\\d+)(\\S+)(\\d+):(\\d+)";
        // number before book, book name, "chapter", and chapter number
        String patt5 = "((\\d+))(\\S+) chapter (\\d+)";
        // book, chapter, ":", verse
        String patt6 = "((\\S+))(\\d+):(\\d+)";
        
        Pattern patrn1 =Pattern.compile(patt1);
        Pattern patrn2 =Pattern.compile(patt2);
        Pattern patrn3 =Pattern.compile(patt3);
        Pattern patrn4 =Pattern.compile(patt4);
        Pattern patrn5 =Pattern.compile(patt5);
        Pattern patrn6 =Pattern.compile(patt6);
        
        Matcher match1 = patrn1.matcher(line);
        Matcher match2 = patrn2.matcher(line);
        Matcher match3 = patrn3.matcher(line);
        Matcher match4 = patrn4.matcher(line);
        Matcher match5 = patrn5.matcher(line);
        Matcher match6 = patrn6.matcher(line);
        
        while (match1.find()) {
            tmp = match1.group();
            tmp = tmp.replace(" chapter", ":");
            if (validScripture(tmp, scriptures) && !scripture.contains(tmp)) {
                scripture.add(tmp);
            }
        }
        
        while (match2.find()) {
            tmp = match2.group();
            if (validScripture(tmp, scriptures) && !scripture.contains(tmp)) {
                scripture.add(tmp);
            }
        }
        
        while (match3.find()) {
            tmp = match3.group();
            if (validScripture(tmp, scriptures) && !scripture.contains(tmp)) {
                scripture.add(tmp);
            }
        }
        
        while (match4.find()) {
            tmp = match4.group();
            if (validScripture(tmp, scriptures) && !scripture.contains(tmp)) {
                scripture.add(tmp);
            }
        }
        
        while (match5.find()) {
            tmp = match5.group();
            tmp = tmp.replace(" chapter", ":");
            if (validScripture(tmp, scriptures) && !scripture.contains(tmp)) {
                scripture.add(tmp);
            }
        }
        
        while (match6.find()) {
            tmp = match6.group();
            if (validScripture(tmp, scriptures) && !scripture.contains(tmp)) {
                scripture.add(tmp);
            }
        }  
        return scripture;
    }
    
    /**************************************************************************
     * VALID SCRIPTURE
     * This function will check for matches in the scriptures and if it is good
     * then it will be marked as a valid scripture.
     * @param scripture
     * @param scriptures
     * @return 
     *************************************************************************/
    public Boolean validScripture(String scripture, 
                                  Map<String, List<String>> scriptures) {
        String[] parts;
        String book = "";
        
        if (scripture.contains(":")) {
            parts = scripture.split(":");
            if (parts[0].matches("\\d \\w+ \\d+")) {
                parts = parts[0].split(" ");
                book = parts[0] + " " + parts[1];
            }
            else {
                parts = parts[0].split(" ");
                book = parts[0];
            }
        }
        else if (scripture.matches("\\d+ \\w+ \\d+")) {
            parts = scripture.split(" ");
            book = parts[0] + " " + parts[1];
        }
        else {
            parts = scripture.split(" ");
            book = parts[0];
        }
        
        for (String key : scriptures.keySet()) {
            if (book.equals(key)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**************************************************************************
     * WRITE FILE
     * This function will write the file according to the milestone specs.
     * @param entry
     * @param file
     * @throws FileNotFoundException 
     **************************************************************************/
    public void writeFile(List<Entry> entry, String file) throws FileNotFoundException {
        PrintWriter write = new PrintWriter(file);
        String content = "";
        
        for (Entry e : entry) {
            write.println("-----");
            write.println(e.getDate());
            content = e.getContent();
            content = content.trim();
            content = content.replaceAll("\\t+", "\n");
            write.println(content + "\n");
        }
        System.out.println("Content: " + content);
        write.close();
    }
    
    /*************************************************************************
     * BUILD XML
     * This function is in charge of making the XML from a journal. The file will
     * be looked over and the appropriate tags will be created to make a good 
     * looking XML file.
     * @param entry
     * @param file
     * @throws Exception 
     ************************************************************************/
    public void buildXML(List<Entry> entry, String file) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        
        Element root = document.createElement("journal");
        document.appendChild(root);
        
        try {
        // start to create the other elements for the XML document
        for (Entry ent : entry) {
            Element entryTag = document.createElement("entry");
            entryTag.setAttribute("date", ent.getDate());
            root.appendChild(entryTag);
            
            for (String scrip : ent.getScriptures()) {
                Element scripTag = document.createElement("scripture");
                entryTag.setAttribute("book", scrip);
                root.appendChild(scripTag);
            }
            
            for (String topic : ent.getTopic()) {
                Element topicTag = document.createElement("topic");
                Text topics = document.createTextNode(topic);
                topicTag.appendChild(topics);
                entryTag.appendChild(topicTag);
            }
            
            Element content = document.createElement("content");
            Text text = document.createTextNode(ent.getContent());
            content.appendChild(text);
            entryTag.appendChild(content);
        }
        saveDoc(document, file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
     }
    
    /*************************************************************************
     * SAVE DOC
     * This will invoke the correct guidelines in making an XML file to ensure 
     * that the XML guidelines are being followed.
     * @param document
     * @param file
     * @throws Exception 
     ************************************************************************/
    public void saveDoc(Document document, String file) throws Exception {
        System.out.println("Saving file: " + file);
        
        Transformer tran = TransformerFactory.newInstance().newTransformer();
        tran.setOutputProperty(OutputKeys.INDENT, "yes");
        tran.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        Result output = new StreamResult(new File(file));
        Source input = new DOMSource(document);
        
        tran.transform(input, output);
    }
    
    /*************************************************************************
     * DISPLAY DOC
     * This will display the XML for the user to see.
     * @param doc
     * @throws Exception 
     ************************************************************************/
    public void displayDoc(Document doc) throws Exception {
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer output = new StringWriter();
        trans.transform(new DOMSource(doc), new StreamResult(output));
        System.out.println(output.toString());
    }
}