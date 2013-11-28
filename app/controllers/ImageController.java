package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Content;
import models.User;

import org.h2.util.IOUtils;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import resModles.ResContent;
import resResults.ContentResult;
import Contants.HttpContants;

import com.google.gson.Gson;

public class ImageController extends Controller{
	
	public static Result getImage(String path) {
        return ok(new File(path));
	}
}
