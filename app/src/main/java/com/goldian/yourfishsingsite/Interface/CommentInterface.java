package com.goldian.yourfishsingsite.Interface;

import com.goldian.yourfishsingsite.Model.CommentModel;
import com.goldian.yourfishsingsite.Model.Holder.CommentHolder;

public interface CommentInterface {
    void postReply(final CommentModel currentData, final CommentHolder holder);
}
