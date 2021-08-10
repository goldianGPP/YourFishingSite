package com.goldian.yourfishsingsite.Model;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {
    String id_comment, id_reply, id_pengguna, reply_to, username, replier, comment, reply, replies, vote, created_at;
    List<CommentModel> commentModels;

    public String getId_comment() {
        return id_comment;
    }

    public void setId_comment(String id_comment) {
        this.id_comment = id_comment;
    }

    public String getId_reply() {
        return id_reply;
    }

    public void setId_reply(String id_reply) {
        this.id_reply = id_reply;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReply_to() {
        return reply_to;
    }

    public void setReply_to(String reply_to) {
        this.reply_to = reply_to;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
