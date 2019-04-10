from django.shortcuts import render
from . import serializers
from . import models
from rest_framework import generics, views, status
from rest_framework.response import Response
import time
# Create your views here.

class UserListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.User.objects.select_related()
	serializer_class = serializers.UserSerializer

	def get_serializer_class(self):
		assert self.serializer_class is not None,(
			"'%s' should either include a `serializer_class` attribute, "
			"or override the `get_serializer_class()` method."
			% self.__class__.__name__
		)
		if self.request.method == "GET":
			return serializers.UserRegionSerializer

		return self.serializer_class


class UserNameAPIView(views.APIView):

	def get(self, request, format=None):
		# print(request.data)
		data = dict(request.query_params)
		user = models.User.objects.filter(first_name=data.get('first_name')[0],last_name=data.get('last_name')[0])
		if not user.exists():
			#print(type(request.data))
			curr_time = time.time()
			request.data['password']=data.get('last_name')[0]
			request.data['username']=data.get('first_name')[0] +  str(curr_time)
			request.data['first_name']=data.get('first_name')[0]
			request.data['last_name']=data.get('last_name')[0]

			serializer = serializers.UserSerializer(data=request.data)
			if serializer.is_valid():
				serializer.save()
				user = models.User.objects.filter(first_name=data.get('first_name')[0],last_name=data.get('last_name')[0])
			else:
				print(serializer.errors)
				return Response({'msg':'failed'}, status = status.HTTP_404_NOT_FOUND)
		id = user.first().id
		print('The idis', id)
		return Response(id)

# class ReportAPIView(views.APIView):

# 	def post(self, request, format=None):
# 		json = json.loads(request.body)




class UserUpdateAPIView(generics.UpdateAPIView):
	queryset = models.User.objects.all()
	serializer_class = serializers.UserSerializer


class RegionListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.Region.objects.all()
	serializer_class = serializers.RegionSerializer


class RegionUpdateAPIView(generics.UpdateAPIView):
	queryset = models.Region.objects.all()
	serializer_class = serializers.RegionSerializer


class ReportListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.Report.objects.all()
	serializer_class = serializers.ReportSerializer


class Report2ListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.Report2.objects.all()
	serializer_class = serializers.Report2Serializer

	def post(self, request, *args, **kwargs):
		print(request.data)
		return self.create(request, *args, **kwargs)


class WaterbodyReportListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.WaterbodyReport.objects.all()
	serializer_class = serializers.WaterbodyReportSerializer


class PredictListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.Predict.objects.all()
	serializer_class = serializers.PredictSerializer


class HospitalListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.Hospital.objects.select_related()
	serializer_class = serializers.HospitalSerializer

	def get_serializer_class(self):
		assert self.serializer_class is not None,(
			"'%s' should either include a `serializer_class` attribute, "
			"or override the `get_serializer_class()` method."
			% self.__class__.__name__
		)
		if self.request.method == "GET":
			return serializers.HospitalRegionSerializer

		return self.serializer_class


class HospitalUpdateDestroyAPIView(generics.RetrieveUpdateDestroyAPIView):
	queryset = models.Hospital.objects.all()
	serializer_class = serializers.HospitalSerializer


class UserUpdateDestroyAPIView(generics.RetrieveUpdateDestroyAPIView):
	queryset = models.User.objects.all()
	serializer_class = serializers.UserSerializer


class HospitalCurrentListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.HospitalCurrent.objects.all()
	serializer_class = serializers.HospitalCurrentSerializer


class HeatmapActualListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.HeatmapActual.objects.all()
	serializer_class = serializers.HeatmapActualSerializer


class HeatmapPredictedListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.HeatmapPredicted.objects.all()
	serializer_class = serializers.HeatmapPredictedSerializer


class NewsListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.News.objects.all()
	serializer_class = serializers.NewsSerializer


class LabListCreateAPIView(generics.ListCreateAPIView):
	queryset = models.Lab.objects.select_related()
	serializer_class = serializers.LabSerializer

	def post(self,request,*args,**kwargs):
		print(request.data)
		return self.create(request, *args, **kwargs)

	def get_serializer_class(self):
		assert self.serializer_class is not None,(
			"'%s' should either include a `serializer_class` attribute, "
			"or override the `get_serializer_class()` method."
			% self.__class__.__name__
		)
		if self.request.method == "GET":
			return serializers.LabRegionSerializer

		return self.serializer_class

