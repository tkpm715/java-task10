# 第１０回講座課題  

Spring Boot RestAPIのCRUD処理を実装  

---

## プロジェクト構成

Java 11  
Spring Boot 2.7.1  
Gradle  
Docker  
MySQL 8.0  

---
## DBテーブル
テーブル名：users  

| カラム名 | データ型        | NotNull  | 備考                    |
----|-----|-------|------------
| id | INT         | NOT NULL | ID・主キー・Auto Increment |
| name | VARCHAR(50) | NOT NULL | 名前                    |
| postcode | CHAR(7)  | NOT NULL | 郵便番号                  |
---
## 動作概要

### ▼データ全件取得 (初期状態)  
![2022-07-21_23h38_59](https://user-images.githubusercontent.com/101798620/180392663-311a33c6-991d-4f8f-b48a-ff4c87f9bf5a.png)


### ▼データ１件取得  
パスパラメータにIDを設定し、該当ユーザーデータを取得。  更にそのデータに含まれる郵便番号に対する住所を外部APIから取得し、ユーザーデータと一緒にJson出力する。  
![2022-07-21_23h40_27](https://user-images.githubusercontent.com/101798620/180392814-3111b1a7-b35f-4b96-a8af-a0ef6b35cad8.png)
    
※ 存在しないIDをパスパラメータに指定した場合404を返す。  
![2022-07-21_23h51_07](https://user-images.githubusercontent.com/101798620/180393404-c8f32d98-3217-4880-badc-a146dfa08c92.png)
  

### ▼データ新規作成  
![2022-07-21_23h42_43](https://user-images.githubusercontent.com/101798620/180395240-a3081866-d4f0-478e-a039-c63fc6a17be9.png)
    
※ 名前の入力が1～50文字でなかった場合400を返す。  
![2022-07-21_23h49_51](https://user-images.githubusercontent.com/101798620/180443225-de71414f-3bf0-4746-8d1d-cb8820297660.png)

    
※ 郵便番号の入力が半角英数７桁でなかった場合400を返す。  
![2022-07-21_23h50_38](https://user-images.githubusercontent.com/101798620/180396334-0e381c5d-d9b2-43e5-b7ce-e5db3d4dccb9.png)


### ▼データ更新  
![2022-07-21_23h44_05](https://user-images.githubusercontent.com/101798620/180396540-6acab92d-d930-41ff-9d71-13dff7bf3250.png)


### ▼データ削除  
![2022-07-21_23h44_36](https://user-images.githubusercontent.com/101798620/180396647-558d9bbe-ffe3-43f6-a4c1-8d296c73d6ab.png)

### ▼データ初期状態 →データ作成(ID3) →ID3データ更新 →ID1データ削除 →現在の状態  
![2022-07-21_23h44_56](https://user-images.githubusercontent.com/101798620/180397225-2d768b2b-a552-47d8-9102-1bb350e764eb.png)

