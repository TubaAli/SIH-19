# Generated by Django 2.1.7 on 2019-03-02 09:35

from django.conf import settings
import django.contrib.auth.models
import django.core.validators
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('db', '0004_auto_20190302_1503'),
    ]

    operations = [
        migrations.CreateModel(
            name='HeatmapActual',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('intensity', models.IntegerField(default=5, validators=[django.core.validators.MaxValueValidator(10), django.core.validators.MinValueValidator(0)])),
                ('pincode', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='db.Region')),
            ],
        ),
        migrations.CreateModel(
            name='HeatmapPredicted',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('intensity', models.IntegerField(default=5, validators=[django.core.validators.MaxValueValidator(10), django.core.validators.MinValueValidator(0)])),
                ('pincode', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='db.Region')),
            ],
        ),
        migrations.CreateModel(
            name='Hospital',
            fields=[
                ('user_ptr', models.OneToOneField(auto_created=True, on_delete=django.db.models.deletion.CASCADE, parent_link=True, primary_key=True, serialize=False, to=settings.AUTH_USER_MODEL)),
                ('malaria_free', models.PositiveSmallIntegerField()),
                ('tb_free', models.PositiveSmallIntegerField()),
                ('dengue_free', models.PositiveSmallIntegerField()),
                ('rating', models.IntegerField(default=3, validators=[django.core.validators.MaxValueValidator(5), django.core.validators.MinValueValidator(1)])),
            ],
            options={
                'verbose_name': 'user',
                'verbose_name_plural': 'users',
                'abstract': False,
            },
            bases=('db.user',),
            managers=[
                ('objects', django.contrib.auth.models.UserManager()),
            ],
        ),
        migrations.CreateModel(
            name='HospitalCurrent',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('time', models.DateTimeField(auto_now_add=True)),
                ('malaria_rep', models.PositiveSmallIntegerField()),
                ('tb_rep', models.PositiveSmallIntegerField()),
                ('dengue_rep', models.PositiveSmallIntegerField()),
                ('malaria_dis', models.PositiveSmallIntegerField()),
                ('tb_dis', models.PositiveSmallIntegerField()),
                ('dengue_dis', models.PositiveSmallIntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='Lab',
            fields=[
                ('user_ptr', models.OneToOneField(auto_created=True, on_delete=django.db.models.deletion.CASCADE, parent_link=True, primary_key=True, serialize=False, to=settings.AUTH_USER_MODEL)),
                ('certificate', models.ImageField(upload_to='certificates/user_<built-in function id>/%Y/%m/%d')),
                ('owner', models.CharField(max_length=255)),
                ('govt_id', models.CharField(max_length=255)),
            ],
            options={
                'verbose_name': 'user',
                'verbose_name_plural': 'users',
                'abstract': False,
            },
            bases=('db.user',),
            managers=[
                ('objects', django.contrib.auth.models.UserManager()),
            ],
        ),
        migrations.CreateModel(
            name='News',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateTimeField()),
                ('article_author', models.CharField(max_length=255)),
                ('article_text_path', models.FilePathField(path='/')),
                ('article_img_path', models.FilePathField(path='/')),
                ('pincode', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='db.Region')),
            ],
        ),
        migrations.CreateModel(
            name='Predict',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('prediction_time', models.DateTimeField(auto_now_add=True)),
                ('disease_predicted', models.CharField(max_length=255)),
                ('pincode', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='db.Region')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AddField(
            model_name='hospitalcurrent',
            name='hospital',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL),
        ),
    ]