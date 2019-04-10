from rest_framework import serializers

from . import models


class UserSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.User
		fields = '__all__'


class RegionSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.Region
		fields = '__all__'


class ReportSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.Report
		# exclude = ('pdf_path',)
		fields='__all__'


class Report2Serializer(serializers.ModelSerializer):
	class Meta:
		model = models.Report2
		# exclude = ('pdf_path',)
		fields='__all__'



class WaterbodyReportSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.WaterbodyReport
		exclude = ('image_path',)


class PredictSerializer(serializers.ModelSerializer):
	#user = serializers.PrimaryKeyRelatedField(many=False,read_only=True)
	#pincode = serializers.PrimaryKeyRelatedField(many=False,read_only=True)
	#user = UserSerializer()

	class Meta:
		model = models.Predict
		fields = '__all__'


class HospitalSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.Hospital
		fields = '__all__' 


class HospitalCurrentSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.HospitalCurrent
		fields = '__all__'


class HeatmapActualSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.HeatmapActual
		fields = '__all__'


class HeatmapPredictedSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.HeatmapPredicted
		fields = '__all__'


class NewsSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.News
		exclude = ('article_text_path','article_img_path')


class LabSerializer(serializers.ModelSerializer):
	class Meta:
		model = models.Lab
		exclude = ('certificate',)


class HospitalRegionSerializer(serializers.ModelSerializer):
	pincode = RegionSerializer()
	class Meta:
		model = models.Hospital
		fields = '__all__'


class LabRegionSerializer(serializers.ModelSerializer):
	pincode = RegionSerializer()
	class Meta:
		model = models.Lab
		fields = '__all__'


class UserRegionSerializer(serializers.ModelSerializer):
	pincode = RegionSerializer()
	class Meta:
		model = models.User
		fields = '__all__'