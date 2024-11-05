package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.example.dto.Book;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneOpsTest {

    public static final String LUCENE_INDEX = "D:/temp/lucene/index";

    @Before
    public void setUp() throws Exception {
        // clean directory
        File directory = new File(LUCENE_INDEX);
        deleteDirectory(directory);
    }

    /**
     * 创建索引
     *
     * @throws Exception
     */
    @Test
    public void testCreateIndex() throws Exception {
        // 1.模拟数据采集
        List<Book> bookArrayList = new ArrayList<>();

        Book bookOne = new Book();
        bookOne.setId(1);
        bookOne.setName("Lucene");
        bookOne.setPrice(100.45F);
        bookOne.setDesc("Lucene Core is a Java library providing powerful indexing and search features," +
                "as well as spellchecking, hit highlighting and advanced analysis/tokenization capabilities. " +
                "The PyLucene sub project provides Python bindings for Lucene Core.");

        Book bookTwo = new Book();
        bookTwo.setId(11);
        bookTwo.setName("Solr");
        bookTwo.setPrice(320.45F);
        bookTwo.setDesc("Solr is highly scalable, providing fully fault tolerant distributed indexing, " +
                "search and analytics. It exposes Lucene's features through easy to use JSON/HTTP interfaces or " +
                "native clients for Java and other languages.");

        Book bookThree = new Book();
        bookThree.setId(21);
        bookThree.setName("Hadoop");
        bookThree.setPrice(620.45F);
        bookThree.setDesc("The Apache Hadoop software library is a framework that allows for " +
                "the distributed processing of large data sets across clusters of computers " +
                "using simple programming models.");

        bookArrayList.add(bookOne);
        bookArrayList.add(bookTwo);
        bookArrayList.add(bookThree);

        // 2.创建 Document 文档对象
        List<Document> documents = new ArrayList<>();
        for (Book book : bookArrayList) {
            Document doc = new Document();
            doc.add(new TextField("id", book.getId().toString(), Field.Store.YES));
            doc.add(new TextField("name", book.getName(), Field.Store.YES));
            doc.add(new TextField("price", book.getPrice().toString(), Field.Store.YES));
            doc.add(new TextField("desc", book.getDesc(), Field.Store.YES));

            documents.add(doc);
        }

        // 3.创建Analyzer分词器,分析文档，对文档进行分词
        Analyzer analyzer = new StandardAnalyzer();
        // 创建Directory对象,声明索引库的位置
        Directory directory = FSDirectory.open(Paths.get(LUCENE_INDEX));
        // 创建IndexWriteConfig对象，写入索引需要的配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 4.创建IndexWriter写入对象 添加文档对象document
        IndexWriter indexWriter = new IndexWriter(directory, config);
        for (Document doc : documents) {
            indexWriter.addDocument(doc);
        }
        // 释放资源
        indexWriter.close();
        System.out.println("索引创建成功");

        // 执行方法后指定目录生成的文件如下
        // D:\temp\lucene\index\write.lock
        // D:\temp\lucene\index\_0.cfe
        // D:\temp\lucene\index\_0.cfs
        // D:\temp\lucene\index\_0.si
        // D:\temp\lucene\index\segments_1

    }

    /**
     * 删除指定目录
     *
     * @param dir 待删除的目录
     */
    private void deleteDirectory(File dir) {
        if (dir == null || !dir.exists()) {
            System.out.println("目录不存在: " + dir);
            return;
        }

        // 递归删除目录内容
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            // 防止空指针异常
            if (files != null) {
                for (File file : files) {
                    // 递归删除子文件和目录
                    deleteDirectory(file);
                }
            }
        }

        // 删除当前目录或文件
        dir.delete();
    }
}
