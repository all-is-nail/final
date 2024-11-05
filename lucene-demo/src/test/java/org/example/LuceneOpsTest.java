package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.example.dto.Book;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneOpsTest {

    public static final String LUCENE_INDEX = "D:/temp/lucene/index";
    public static final List<Book> BOOK_ARRAY_LIST = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        System.out.println("执行预删除索引目录");
        // clean directory
        File directory = new File(LUCENE_INDEX);
        deleteDirectory(directory);

        System.out.println("执行模拟数据采集");
        // 模拟数据采集
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

        BOOK_ARRAY_LIST.add(bookOne);
        BOOK_ARRAY_LIST.add(bookTwo);
        BOOK_ARRAY_LIST.add(bookThree);
    }

    /**
     * 创建索引
     *
     * @throws Exception
     */
    @Test
    public void testCreateIndex() throws Exception {
        // 1.模拟数据采集
        // 2.创建 Document 文档对象
        List<Document> documents = new ArrayList<>();
        for (Book book : BOOK_ARRAY_LIST) {
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
     * 创建索引（使用中文分词器 IKAnalyzer）
     *
     * @throws Exception
     */
    @Test
    public void testCreateIndexWithIKAnalyzer() throws Exception {
        // 1.模拟数据采集
        // 2.创建 Document 文档对象
        List<Document> documents = new ArrayList<>();

        Document document = new Document();
        document.add(new TextField("address", "贵州省黔东南苗族侗族自治州凯里市万潮镇", Field.Store.YES));
        document.add(new TextField("desc", "这是一个中文的描述", Field.Store.YES));
        documents.add(document);
        // 3.创建Analyzer分词器,分析文档，对文档进行分词
        // Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
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
    }

    /**
     * 搜索
     */
    @Test
    public void testSearchIndex() throws Exception {
        // 先执行索引创建
        testCreateIndex();

        // 1. 创建Query搜索对象
        // 创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建搜索解析器，第一个参数：默认Field域，第二个参数：分词器
        QueryParser queryParser = new QueryParser("id", analyzer);
        // 创建搜索对象
        Query query = queryParser.parse("desc:java OR name:solr");
        // 2. 创建Directory流对象,声明索引库位置
        Directory directory = FSDirectory.open(Paths.get(LUCENE_INDEX));
        // 3. 创建索引读取对象IndexReader
        IndexReader reader = DirectoryReader.open(directory);
        // 4. 创建索引搜索对象
        IndexSearcher searcher = new IndexSearcher(reader);
        // 5. 使用索引搜索对象，执行搜索，返回结果集TopDocs
        // 第一个参数：搜索对象，第二个参数：返回的数据条数，指定查询结果最顶部的n条数据返回
        TopDocs topDocs = searcher.search(query, 10);
        System.out.println("查询到的数据总条数是：" + topDocs.totalHits);
        // 获取查询结果集
        ScoreDoc[] docs = topDocs.scoreDocs;
        // 6. 解析结果集
        for (ScoreDoc scoreDoc : docs) {
            // 获取文档
            int docID = scoreDoc.doc;
            Document doc = searcher.doc(docID);
            System.out.println("=============================");
            System.out.println("score:" + scoreDoc.score);
            System.out.println("docID:" + docID);
            System.out.println("bookId:" + doc.get("id"));
            System.out.println("name:" + doc.get("name"));
            System.out.println("price:" + doc.get("price"));
            System.out.println("desc:" + doc.get("desc"));
        }
        // 释放资源
        reader.close();
    }

    /**
     * 域操作
     * @throws Exception
     */
    @Test
    public void testCreateIndexWithDifferentFields() throws Exception {
        List<Document> documents = new ArrayList<>();
        BOOK_ARRAY_LIST.forEach(book -> {
            Document doc = new Document();
            // IntPoint 分词 索引 不存储 存储结合 StoredField
            Field id = new IntPoint("id", book.getId());
            Field idStored = new StoredField("id", book.getId());
            // 分词、索引、存储 TextField
            Field name = new TextField("name", book.getName(), Field.Store.YES);
            // 分词、索引、不存储 但是是数字类型，所以使用FloatPoint
            Field price = new FloatPoint("price", book.getPrice());
            // 分词、索引、不存储 TextField
            Field desc = new TextField("desc", book.getDesc(), Field.Store.NO);

            doc.add(id);
            doc.add(idStored);
            doc.add(name);
            doc.add(price);
            doc.add(desc);

            documents.add(doc);
        });

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
     * 基于已有索引新增 document
     *
     * @throws Exception
     */
    @Test
    public void testAddDocument() throws Exception {
        // 先执行索引创建
        testCreateIndexWithDifferentFields();

        // 创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建Directory流对象
        Directory directory = FSDirectory.open(Paths.get(LUCENE_INDEX));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 创建写入对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        // 创建Document
        Document document = new Document();
        document.add(new TextField("id", "1001", Field.Store.YES));
        document.add(new TextField("name", "game", Field.Store.YES));
        document.add(new TextField("desc", "one world one dream", Field.Store.NO));
        // 添加文档 完成索引添加
        indexWriter.addDocument(document);
        // 释放资源
        indexWriter.close();
        System.out.println("基于已有索引新增 document");

        // 执行方法后指定目录生成的文件如下
        // D:\temp\lucene\index\write.lock
        // D:\temp\lucene\index\_0.cfe
        // D:\temp\lucene\index\_0.cfs
        // D:\temp\lucene\index\_0.si
        // D:\temp\lucene\index\_1.cfe
        // D:\temp\lucene\index\_1.cfs
        // D:\temp\lucene\index\_1.si
        // D:\temp\lucene\index\segments_2
    }

    /**
     * 根据 term 删除 document
     *
     * @throws Exception
     */
    @Test
    public void testDeleteDocument() throws Exception {
        testCreateIndexWithDifferentFields();
        Directory directory = FSDirectory.open(Paths.get(LUCENE_INDEX));
        IndexWriterConfig config = new IndexWriterConfig();
        IndexWriter indexWriter = new IndexWriter(directory, config);
        indexWriter.deleteDocuments(new Term("name", "game"));
        indexWriter.close();
        System.out.println("根据 term 删除 document");

        // 执行方法后索引目录文件如下
        // D:\temp\lucene\index\write.lock
        // D:\temp\lucene\index\_0.cfe
        // D:\temp\lucene\index\_0.cfs
        // D:\temp\lucene\index\_0.si
        // D:\temp\lucene\index\segments_1
    }

    /**
     * 更新 document
     * 执行逻辑：先删除匹配到的 document，再添加新的 document
     * @throws Exception
     */
    @Test
    public void testUpdateDocument() throws Exception {
        testCreateIndexWithDifferentFields();
        Directory directory = FSDirectory.open(Paths.get(LUCENE_INDEX));
        IndexWriterConfig config = new IndexWriterConfig();
        IndexWriter indexWriter = new IndexWriter(directory, config);
        Document document = new Document();
        document.add(new TextField("id", "1002", Field.Store.YES));
        document.add(new TextField("name", "测试test update1002", Field.Store.YES));
        indexWriter.updateDocument(new Term("name", "lucene"), document);
        indexWriter.close();
        System.out.println("更新 document");
    }

    /**
     * 删除所有的 document
     *
     * @throws Exception
     */
    @Test
    public void testDeleteAllDocuments() throws Exception {
        testCreateIndexWithDifferentFields();
        Directory directory = FSDirectory.open(Paths.get(LUCENE_INDEX));
        IndexWriterConfig config = new IndexWriterConfig();
        IndexWriter indexWriter = new IndexWriter(directory, config);
        indexWriter.deleteAll();
        indexWriter.close();
        System.out.println("删除所有的 document");

        // 执行方法后索引目录文件如下
        // D:\temp\lucene\index\write.lock
        // D:\temp\lucene\index\segments_2
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
