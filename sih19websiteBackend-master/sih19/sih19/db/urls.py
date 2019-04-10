from django.urls import path

from . import views

urlpatterns = [
	path('user/', views.UserListCreateAPIView.as_view(), name='user_list'),
	path('report2/', views.Report2ListCreateAPIView.as_view(), name='report2_list'),
	path('user/update/<int:pk>/', views.UserUpdateAPIView.as_view(), name='user_update'),
	path('mylogin/', views.UserNameAPIView.as_view(), name='user_api'),
	path('region/', views.RegionListCreateAPIView.as_view(), name='region_list'),
	path('region/update/<int:pk>/', views.RegionUpdateAPIView.as_view(), name='region_update'),
	path('report/', views.ReportListCreateAPIView.as_view(), name='report_list'),
	path('waterbody_report/', views.WaterbodyReportListCreateAPIView.as_view(), name='waterbody_report_list'),
	path('predict/', views.PredictListCreateAPIView.as_view(), name='predict_list'),
	path('hospital/', views.HospitalListCreateAPIView.as_view(), name='hospital_list'),
	path('hospital_current/', views.HospitalCurrentListCreateAPIView.as_view(), name='hospital_current_list'),
	path('heatmap_actual/', views.HeatmapActualListCreateAPIView.as_view(), name='hospital_list'),
	path('heatmap_predicted/', views.HeatmapPredictedListCreateAPIView.as_view(), name='heatmap_predicted_list'),
	path('news/', views.NewsListCreateAPIView.as_view(), name='news_list'),
	path('lab/', views.LabListCreateAPIView.as_view(), name='lab_list'),
	path('hospital_ud/<int:pk>/', views.HospitalUpdateDestroyAPIView.as_view(), name = 'lab_ud'),
	path('user_ud/<int:pk>/', views.UserUpdateDestroyAPIView.as_view(), name = 'user_ud'),
]