package com.goldian.yourfishsingsite.Model;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {
    String id_comment, id_reply, id_pengguna, ids, reply_to, username, replier, comment, reply, replies, vote, created_at;
    List<CommentModel> commentModels;

    public String getId_comment() {
        return id_comment;
    }

    public CommentModel setId_comment(String id_comment) {
        this.id_comment = id_comment;
        return this;
    }

    public String getId_reply() {
        return id_reply;
    }

    public CommentModel setId_reply(String id_reply) {
        this.id_reply = id_reply;
        return this;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public CommentModel setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
        return this;
    }

    public String getIds() {
        return ids;
    }

    public CommentModel setIds(String ids) {
        this.ids = ids;
        return this;
    }

    public String getReplier() {
        return replier;
    }

    public CommentModel setReplier(String replier) {
        this.replier = replier;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CommentModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getReply_to() {
        return reply_to;
    }

    public CommentModel setReply_to(String reply_to) {
        this.reply_to = reply_to;
        return this;
    }

    public List<CommentModel> getCommentModels() {
        return commentModels;
    }

    public void setCommentModels() {
        this.commentModels = new ArrayList<>();
    }

    public void addAllCommentModels(List<CommentModel> commentModels) {
        this.commentModels.addAll(commentModels);
    }

    public void addCommentModels(CommentModel commentModel) {
        this.commentModels.add(commentModel);
    }

    public String getComment() {
        return comment;
    }

    public CommentModel setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getReply() {
        return reply;
    }

    public CommentModel setReply(String reply) {
        this.reply = reply;
        return this;
    }

    public String getReplies() {
        return replies;
    }

    public CommentModel setReplies(String replies) {
        this.replies = replies;
        return this;
    }

    public String getVote() {
        return vote;
    }

    public CommentModel setVote(String vote) {
        this.vote = vote;
        return this;
    }

    public String getCreated_at() {
        return created_at;
    }

    public CommentModel setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }
}
