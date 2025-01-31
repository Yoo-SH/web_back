const mongodb = require('mongodb');
const db = require('../data/database');

const ObjectId = mongodb.ObjectId;

class Post {
    constructor(title, content, id){
        this.title = title;
        this.content = content;
        this.id = id;

        if(id){
            this.id = new ObjectId(id)
        }
    }
    
    static async fetchAll(){
        const posts = db.getDb().collection('posts').find().toArray();
        return posts;
    }

    async fetch(){
        if (!this.id)
            return;

        const postDocoment = await db.getDb().collection('posts').findOne({ _id: this.id });
        this.title = postDocoment.title;
        this.content = postDocoment.content;        


    }

    async save(){
        let result;
        if(this.id){
            result = db.getDb().collection('posts').updateOne(
                { _id: this.id },
                { $set: {title : this.title , content : this.content} }
            );
        } else {
            result = db.getDb().collection('posts').insertOne({
                title: this.title,
                content: this.content
            });
        }
        
        
        return result;
    }

    async delete(){
        if(!this.id){
            return 
        }
        const result = db
        .getDb()
        .collection('posts')
        .deleteOne({ _id: this.id });
        return result;
    }
}


module.exports = Post;