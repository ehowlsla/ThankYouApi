package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Content;
import models.ContentLike;
import models.Notice;
import models.Reply;
import models.ReplyLike;
import models.User;

import org.h2.util.IOUtils;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import resModles.ResContent;
import resModles.ResContentLike;
import resModles.ResReply;
import resResults.ContentResult;
import utils.ThumbnailGenerator;
import Contants.HttpContants;
import com.google.gson.Gson;

public class ContentController extends Controller {

	public static Result list(String user_id, String target_id, String last_id,
			String openLevel) {
		ContentResult result = new ContentResult();

		List<Content> contents = Content.getContentList(
				Long.parseLong(user_id), Long.parseLong(target_id),
				Long.parseLong(last_id), Integer.parseInt(openLevel));
		if (contents != null) {
			result.code = HttpContants.OK_200;
			result.msg = "타임라인 정보를 가져왔습니다.";

			for (Content obj : contents) {

				ResContent content = new ResContent(obj);
				content.isLike = ContentLike.getUserLike(
						Long.parseLong(user_id), obj.id);
				result.body.add(content);
			}
		} else {
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "타임라인 정보가 더이상 존재하지 않습니다.";
		}

		return ok(new Gson().toJson(result));
	}



	public static Result contentLike() {
		ContentResult result = new ContentResult();

		Map<String, String[]> params = request().body().asFormUrlEncoded();
		Long user_id = Long.parseLong(params.get("user_id")[0]);
		Long content_id = Long.parseLong(params.get("content_id")[0]);

		Content content = Content.getContentDetail(content_id);
		if (content != null) {
			User user = User.getUserInfo(user_id);
			if (user != null) {
				ContentLike like = ContentLike.getLike(user_id, content_id);
				if (like != null) {
					like.delete();
					int likeCount = content.likeCount;
					content.likeCount = likeCount - 1;
					content.update();

					result.msg = "추천을 취소하였습니다.";
				} else {
					like = new ContentLike(user, content_id);
					like.save();

					int likeCount = content.likeCount;
					content.likeCount = likeCount + 1;
					content.update();

					String nickname = user.nickname;
					if (nickname.length() == 0)
						nickname = "";

					if (user_id != content.user.id) {
						Notice notice = new Notice(content.id, content.user.id,
								nickname + "님이 일기를 좋아합니다.", user.image_url1);
						notice.save();
					}

					result.msg = "추천하였습니다.";
				}

				result.code = HttpContants.OK_200;
				result.body.add(new ResContent(content));
			} else {
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "해당 유저가 존재하지 않습니다.";
			}
		} else {
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 일기가 존재하지 않습니다.";
		}

		return ok(new Gson().toJson(result));
	}

	public static Result getContentDetail(String user_id, String content_id,
			String reply_id) {
		ContentResult result = new ContentResult();

		Content content = Content.getContentDetail(Long.parseLong(content_id));
		if (content != null) {
			List<Reply> replies = Reply.getContentReplies(
					Long.parseLong(content_id), Long.parseLong(reply_id));

			for (Reply obj : replies) {
				ResReply value = new ResReply(obj);
				value.likes = ReplyLike.getLikes(value.id);
				result.replies.add(value);
			}

			List<ContentLike> likes = ContentLike.getLikes(Long
					.parseLong(content_id));
			for (ContentLike obj : likes) {
				ResContentLike value = new ResContentLike(obj);
				result.likes.add(value);
			}

			result.code = HttpContants.OK_200;
			result.msg = "성공적으로 일기 정보를 가져왔습니다.";
			result.body.add(new ResContent(content));
		} else {
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 일기가 존재하지 않습니다.";
		}

		return ok(new Gson().toJson(result));
	}

	public static Result upload() {
		ContentResult result = new ContentResult();

		String imageURL1 = "";
		String imageURL2 = "";
		String imageURL3 = "";
		String imageURL4 = "";

		int num = 1;
		if (request().body().asMultipartFormData() != null) {
			Map<String, String[]> params = request().body()
					.asMultipartFormData().asFormUrlEncoded();

			Long user_id = Long.parseLong(params.get("user_id")[0]);
			String contents = params.get("contents")[0];
			int openLevel = Integer.parseInt(params.get("openLevel")[0]);

			User user = User.getUserInfo(user_id);
			if (user != null) {
				List<FilePart> uploadFiles = request().body()
						.asMultipartFormData().getFiles();

				for (FilePart part : uploadFiles) {
					if (part != null) {
						File file = part.getFile();
						Date date = new Date();
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyyMMdd_HHmmss");
						String imageURL = "Images/Contents/" + user_id + "_"
								+ format.format(date) + "_"
								+ String.valueOf(num) + ".JPG";
						String s_imageURL = "Images/Contents/thumbnail_"
								+ user_id + "_" + format.format(date) + "_"
								+ String.valueOf(num) + ".JPG";

						File saveFile = new File(imageURL);
						FileInputStream is;
						try {
							is = new FileInputStream(file);
							IOUtils.copy(is, new FileOutputStream(saveFile));

							ThumbnailGenerator generator = new ThumbnailGenerator();
							generator.transform(imageURL, s_imageURL, 480, 480);

							if (num == 1) {
								imageURL1 = s_imageURL;
							} else if (num == 2) {
								imageURL2 = s_imageURL;
							} else if (num == 3) {
								imageURL3 = s_imageURL;
							} else if (num == 4) {
								imageURL4 = s_imageURL;
							}
							num++;
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

				Content content = new Content(user, contents, imageURL1,
						imageURL2, imageURL3, imageURL4, openLevel);
				content.save();

				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 업로드 되었습니다.";
				result.body.add(new ResContent(content));
			} else {
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "해당 유저가 없습니다.";
			}
		} else if(request().body().asFormUrlEncoded() != null){
			Map<String, String[]> params = request().body().asFormUrlEncoded();
			Long user_id = Long.parseLong(params.get("user_id")[0]);
			String contents = params.get("contents")[0];
			int openLevel = Integer.parseInt(params.get("openLevel")[0]);

			User user = User.getUserInfo(user_id);
			if (user != null) {
				Content content = new Content(user, contents, openLevel);
				content.save();
				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 업로드 되었습니다.";
				result.body.add(new ResContent(content));
			} else {
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "해당 유저가 없습니다.";
			}
		}else{
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "형식이 잘못되었습니다.";
		}

		return ok(new Gson().toJson(result));
	}

	public static Result delete() {
		ContentResult result = new ContentResult();

		Map<String, String[]> params = request().body().asFormUrlEncoded();
		Long content_id = Long.parseLong(params.get("content_id")[0]);

		Content content = Content.getContentDetail(content_id);
		if (content != null) {
			if (content.status == 0) {
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "이미 삭제된 컨텐츠입니다.";
			} else {
				content.status = 0;
				content.update();
				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 삭제되었습니다.";
			}
		} else {
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 유저가 없습니다.";
		}

		return ok(new Gson().toJson(result));
	}

	public static Result ban() {
		ContentResult result = new ContentResult();

		Map<String, String[]> params = request().body().asFormUrlEncoded();
		Long user_id = Long.parseLong(params.get("user_id")[0]);
		Long content_id = Long.parseLong(params.get("content_id")[0]);

		Content content = Content.getContentDetail(content_id);
		if (content != null) {
			int viewCount = content.banCount + 1;
			content.banCount = viewCount + 1;
			if (content.banCount > 3) {
				content.status = 0;
			}

			content.update();

			result.code = HttpContants.OK_200;
			result.msg = "성공적으로 신고되었습니다.";
			result.body.add(new ResContent(content));
		} else {
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "잘못된 게시글이거나 이미 삭제되었습니다.";
		}

		return ok(new Gson().toJson(result));
	}

	public static Result modify() {
		ContentResult result = new ContentResult();

		String imageURL1 = "";
		String imageURL2 = "";
		String imageURL3 = "";
		String imageURL4 = "";
		int modify_status = 1;

		if (request().body().asMultipartFormData() != null) {
			Map<String, String[]> params = request().body()
					.asMultipartFormData().asFormUrlEncoded();
			Long user_id = Long.parseLong(params.get("user_id")[0]);
			Long content_id = Long.parseLong(params.get("content_id")[0]);
			String contents = params.get("contents")[0];
			int openLevel = Integer.parseInt(params.get("openLevel")[0]);
			Content content = Content.getContentDetail(content_id);
			if(params.get("modify_status") != null){
				modify_status =Integer.parseInt(params.get("modify_status")[0]);
			}

			int num = modify_status;
			User user = User.getUserInfo(user_id);
			if (user != null) {
				List<FilePart> uploadFiles = request().body()
						.asMultipartFormData().getFiles();

				for (FilePart part : uploadFiles) {
					if (part != null) {
						File file = part.getFile();
						Date date = new Date();
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyyMMdd_HHmmss");
						String imageURL = "Images/Contents/" + user_id + "_"
								+ format.format(date) + "_"
								+ String.valueOf(num) + ".JPG";
						String s_imageURL = "Images/Contents/thumbnail_"
								+ user_id + "_" + format.format(date) + "_"
								+ String.valueOf(num) + ".JPG";

						File saveFile = new File(imageURL);
						FileInputStream is;
						try {
							is = new FileInputStream(file);
							IOUtils.copy(is, new FileOutputStream(saveFile));

							ThumbnailGenerator generator = new ThumbnailGenerator();
							generator.transform(imageURL, s_imageURL, 480, 480);


							if(modify_status == 1){
								if (num == 1) {
									imageURL1 = s_imageURL;
									content.imageURL1 = imageURL1;
								} else if (num == 2) {
									imageURL2 = s_imageURL;
									content.imageURL2 = imageURL2;
								} else if (num == 3) {
									imageURL3 = s_imageURL;
									content.imageURL3 = imageURL3;
								} else if (num == 4) {
									imageURL4 = s_imageURL;
									content.imageURL4 = imageURL4;
								}
							}else if(modify_status == 2){
								if (num == 2) {
									imageURL1 = s_imageURL;
									content.imageURL2 = imageURL1;
								} else if (num == 3) {
									imageURL2 = s_imageURL;
									content.imageURL3 = imageURL2;
								} else if (num == 4) {
									imageURL3 = s_imageURL;
									content.imageURL4 = imageURL3;
								}
							}else if(modify_status == 3){
								if (num == 3) {
									imageURL1 = s_imageURL;
									content.imageURL3 = imageURL1;
								} else if (num == 4) {
									imageURL2 = s_imageURL;
									content.imageURL4 = imageURL2;
								}
							}else if(modify_status == 4){
								if (num == 4) {
									imageURL1 = s_imageURL;
									content.imageURL4 = imageURL1;
								}
							}
							num++;
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

				if (content != null) {
					content.contents = contents;
					content.open_level = openLevel;
					content.update();
					result.code = HttpContants.OK_200;
					result.msg = "성공적으로 수정되었습니다.";
					result.body.add(new ResContent(content));
				} else {
					result.code = HttpContants.FORBIDDEN_403;
					result.msg = "잘못된 게시글이거나 이미 삭제되었습니다.";
				}

			} else {
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "해당 유저가 없습니다.";
			}

		} else if(request().body().asFormUrlEncoded() != null){
			Map<String, String[]> params = request().body().asFormUrlEncoded();
			Long user_id = Long.parseLong(params.get("user_id")[0]);
			Long content_id = Long.parseLong(params.get("content_id")[0]);
			String contents = params.get("contents")[0];
			int openLevel = Integer.parseInt(params.get("openLevel")[0]);
			Content content = Content.getContentDetail(content_id);
			User user = User.getUserInfo(user_id);
			if (user != null) {
				if (content != null) {
					content.contents = contents;
					content.open_level = openLevel;

					content.update();

					result.code = HttpContants.OK_200;
					result.msg = "성공적으로 수정되었습니다.";
					result.body.add(new ResContent(content));
				} else {
					result.code = HttpContants.FORBIDDEN_403;
					result.msg = "잘못된 게시글이거나 이미 삭제되었습니다.";
				}

			} else {
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "해당 유저가 없습니다.";
			}
		}else{
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "형식이 잘못 되었습니다..";
		}

		return ok(new Gson().toJson(result));
	}


}
