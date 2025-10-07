package com.notwork.notwork_backend.operation;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.DropCollectionReq;
import io.milvus.v2.service.index.request.CreateIndexReq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MilvusOperation {

    private String endpoint = "http://localhost:19530";
    private String token = "root:Milvus";

    public void createCollection() {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(endpoint)
                .token(token).build();
        MilvusClientV2 client = new MilvusClientV2(connectConfig);

        CreateCollectionReq.CollectionSchema schema = client.createSchema();
        // 添加字段
        schema.addField(AddFieldReq.builder()
                .fieldName("id")
                .dataType(DataType.VarChar)
                .isPrimaryKey(true)
                .autoID(false)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("blogId")
                .dataType(DataType.Int64)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("blogVector")
                .dataType(DataType.FloatVector)
                .dimension(1024)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("blogChunk")
                .dataType(DataType.VarChar)
                .build());

        List<IndexParam> indexParams = new ArrayList<>();
        indexParams.add(IndexParam.builder().fieldName("blogVector").indexType(IndexParam.IndexType.AUTOINDEX).build());
        indexParams.add(IndexParam.builder().fieldName("blogId").indexType(IndexParam.IndexType.AUTOINDEX).build());
        CreateCollectionReq blogCollection = CreateCollectionReq.builder()
                .collectionName("blogCollection")
                .collectionSchema(schema)
                .indexParams(indexParams)
                .build();

        client.createCollection(blogCollection);
    }

    public void deleteCollection(String collectionName) {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(endpoint)
                .token(token).build();
        MilvusClientV2 client = new MilvusClientV2(connectConfig);
        DropCollectionReq dropQuickSetupParam = DropCollectionReq.builder()
                .collectionName(collectionName)
                .build();

        client.dropCollection(dropQuickSetupParam);
    }

    public static void main(String[] args) {
        MilvusOperation milvusOperation = new MilvusOperation();
        milvusOperation.createCollection();
//        milvusOperation.deleteCollection("blogCollection");
    }
}
