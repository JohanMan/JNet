# jnet
这是一个类似于Retrofit的网络访问框架，个人非常喜欢面向接口编程，Rereofit做到了这点，这就是我要学习Retrofit框架的原因，从而定制出属于自己的“Retrofit”。希望大家能从中学到一些关于AOP和反射机制的知识。

## usage
（1）写一个Service接口类：

    public interface TestService {

        @Get("http://www.baidu.com")
        Call<String> hiBaidu();

    }

（2）写一个MainActivity类：

    public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            TestService testService = JNet.create(TestService.class);
            Call<String> call = testService.hiBaidu();
            call.call(new Callback<String>() {
                @Override
                public void onResponse(final Response<String> respond) {
                    System.out.print("body ----> " + respond.getBody());
                    Toast.makeText(MainActivity.this, "length ----> " + respond.getBody().length(), Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(String reason) {
                    System.out.print("reason ----> " + reason);
                }
            });
        }

    }

就这么简单就可以访问网络了！

### 目前该lib还属于测试阶段，将会实践到下个项目中！！

