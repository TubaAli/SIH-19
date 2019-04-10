from django.contrib.auth.models import AbstractUser
from django.conf import settings
from django.db import models
from django.core.validators import MaxValueValidator, MinValueValidator
import dateutil.parser

# Create your models here.
class Region(models.Model):
	pincode = models.CharField(max_length=6, primary_key=True)
	city = models.CharField(max_length=255, default=None)
	state = models.CharField(max_length=255, default=None)
	longitude = models.FloatField(default=82.9739)
	latitude = models.FloatField(default=25.3176)


class User(AbstractUser):
	GENDER_CHOICES = (
			('M','Male'),
			('F','Female'),
			('O','Other')
		)
	USER_TYPE_CHOICES = (
			('P','Person'),
			('H','Hospital'),
			('L','Lab')
		)
	age = models.IntegerField(null=True)
	gender = models.CharField(GENDER_CHOICES,null=True,max_length=1)
	phone = models.CharField(max_length=13,null=True)
	address = models.TextField(default='') 
	pincode = models.ForeignKey(Region,on_delete=models.CASCADE,null=True)
	type_user = models.CharField(USER_TYPE_CHOICES,max_length=1,null=True)

	def __str__(self):
		return str(self.id) + str(self.first_name)
		#return str(self.id)

	class Meta:
		unique_together = (('first_name','last_name'),)


def user_directory_path(instance,filename):
	return 'reports/user_{0}/user_{1}/{2}/{3}'.format(instance.sentByUser.id, instance.sentForUser.id, 
		dateutil.parser.parse(str(instance.report_time)).date(), filename)


class Report(models.Model):
	sentByUser = models.ForeignKey(settings.AUTH_USER_MODEL,on_delete=models.CASCADE,related_name="by_user")
	sentForUser = models.ForeignKey(settings.AUTH_USER_MODEL,on_delete=models.CASCADE,related_name="for_user")
	report_time = models.DateTimeField(auto_now_add=True)
	disease = models.CharField(max_length=255)
	diagnosis = models.BooleanField()
	#pdf_path = models.FileField(upload_to='reports/user_{0}/user_{1}/%Y/%m/%d'.format(str(sentForUser),str(sentByUser)),max_length=500,null=True)
	pdf_path = models.FileField(upload_to=user_directory_path,null=True)

	def __str__(self):
		return str(self.disease)


def lab_certi_path(instance,filename):
	return 'certificates/user_{0}/{1}/{2}'.format(instance.sent_by_user.id, 
		dateutil.parser.parse(str(instance.report_time)).date(), filename)


class Report2(models.Model):
	GENDER_CHOICES = (
			('M','Male'),
			('F','Female'),
			('O','Other')
		)
	firstName = models.CharField(max_length=255)
	lastName = models.CharField(max_length=255)
	age = models.IntegerField(default=0, blank=True)
	pincode = models.CharField(max_length=255, blank=True)
	number = models.CharField(max_length=255, blank=True)
	#pdf_path = models.FileField(upload_to='reports/user_{0}/user_{1}/%Y/%m/%d'.format(str(sentForUser),str(sentByUser)),max_length=500,null=True)
	address = models.TextField(blank=True)
	dengueBed = models.IntegerField(default = 0, blank=True)
	malariaBed = models.IntegerField(default = 0, blank=True)
	tuberculosisBed = models.IntegerField(default = 0, blank=True)
	owner = models.CharField(max_length=255, blank=True)
	govt_id = models.CharField(max_length=255, blank=True)
	adhaar = models.CharField(max_length=255, blank=True)
	docRegNo = models.CharField(max_length=255, blank=True)
	disease = models.CharField(max_length=255, blank=True)
	hosName = models.CharField(max_length=255, blank=True)
	HosReg = models.CharField(max_length=255, blank=True)
	accreditation = models.CharField(max_length=255, blank=True)
	report_upload = models.FileField(upload_to=user_directory_path,null=True, blank=True)
	certi_upload = models.ImageField(upload_to=lab_certi_path,null=True, blank=True)
	report_disease = models.CharField(max_length=255, blank=True)
	gender = models.CharField(GENDER_CHOICES,null=True,max_length=1, blank=True)

	def __str__(self):
		return str(self.disease)



def user_image_path(instance,filename):
	return 'images/user_{0}/{1}/{2}'.format(instance.sent_by_user.id, 
		dateutil.parser.parse(str(instance.report_time)).date(), filename)


class WaterbodyReport(models.Model):
	sent_by_user = models.ForeignKey(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
	report_time = models.DateTimeField(auto_now_add=True)
	bodyname = models.CharField(max_length=255)
	rating = models.IntegerField(validators=[MaxValueValidator(10),MinValueValidator(1)],default=5)
	pincode = models.ForeignKey(Region,on_delete=models.CASCADE,null=True)
	image_path = models.ImageField(upload_to=user_image_path,null=True)

	def __str__(self):
		return str(self.bodyname)


class Predict(models.Model):
	user = models.ForeignKey(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
	prediction_time = models.DateTimeField(auto_now_add=True)
	disease_predicted = models.CharField(max_length=255)
	pincode = models.ForeignKey(Region,on_delete=models.CASCADE)


class Hospital(User):
	malaria_free = models.PositiveSmallIntegerField()
	tb_free = models.PositiveSmallIntegerField()
	dengue_free = models.PositiveSmallIntegerField()
	rating = models.IntegerField(validators=[MaxValueValidator(5),MinValueValidator(1)],default=3)


class HospitalCurrent(models.Model):
	hospital = models.ForeignKey(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
	time = models.DateTimeField(auto_now_add=True)
	malaria_rep = models.PositiveSmallIntegerField()
	tb_rep = models.PositiveSmallIntegerField()
	dengue_rep = models.PositiveSmallIntegerField()
	malaria_dis = models.PositiveSmallIntegerField()
	tb_dis = models.PositiveSmallIntegerField()
	dengue_dis = models.PositiveSmallIntegerField()


class HeatmapActual(models.Model):
	pincode = models.ForeignKey(Region,on_delete=models.CASCADE)
	intensity = models.IntegerField(validators=[MaxValueValidator(10),MinValueValidator(0)],default=5)


class HeatmapPredicted(models.Model):
	pincode = models.ForeignKey(Region,on_delete=models.CASCADE)
	intensity = models.IntegerField(validators=[MaxValueValidator(10),MinValueValidator(0)],default=5)


class News(models.Model):
	date = models.DateTimeField()
	article_author = models.CharField(max_length=255)
	article_text_path = models.FilePathField(path="/")
	article_img_path = models.FilePathField(path="/")
	pincode = models.ForeignKey(Region,on_delete=models.CASCADE)


class Lab(User):
	certificate = models.ImageField(upload_to=lab_certi_path,null=True)
	owner = models.CharField(max_length=255,blank=True,null=True)
	govt_id = models.CharField(max_length=255,blank=True,null=True)
	free = models.BooleanField(default=False)






	