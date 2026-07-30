  import 'package:flutter/material.dart';
  import 'package:image_text_reader/image_text_reader.dart';
  import 'dart:io';

  import 'package:image_picker/image_picker.dart';

  void main() {
    runApp(const MyApp());
  }

  class MyApp extends StatelessWidget {
    const MyApp({super.key});

    @override
    Widget build(BuildContext context) {
      return MaterialApp(home: HomePage());
    }
  }

  class HomePage extends StatefulWidget {
    const HomePage({super.key});

    @override
    State<HomePage> createState() => _HomePageState();
  }

  class _HomePageState extends State<HomePage> {
    String output = "Waiting...";

    File? selectedImage;
    Future<void> testPlugin() async {
      if(selectedImage == null){

        setState((){

          output = "Select an image first";

        });

        return;

      }


      final result =
      await ImageTextReader.extractText(
          selectedImage!.path
      );
      setState(() {
        output = result.text;
      });
    }

    Future<void> pickImage() async {


      final picker = ImagePicker();


      final image =
      await picker.pickImage(
        source: ImageSource.gallery,
      );


      if(image == null){
        return;
      }


      setState((){

        selectedImage =
            File(image.path);

      });


    }

    @override
    Widget build(BuildContext context) {
      return Scaffold(
        appBar: AppBar(title: const Text("Image Text Reader Test")),

        body: SafeArea(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [

                if (selectedImage != null)
                  Image.file(
                    selectedImage!,
                    width: 200,
                    height: 200,
                  ),

                const SizedBox(height: 20),

                Container(
                  width: double.infinity,
                  constraints: const BoxConstraints(
                    maxHeight: 300,
                  ),
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    border: Border.all(
                      color: Colors.grey,
                    ),
                    borderRadius: BorderRadius.circular(8),
                  ),

                  child: SingleChildScrollView(
                    child: Text(
                      output,
                      style: const TextStyle(
                        fontSize: 16,
                      ),
                    ),
                  ),
                ),

                const SizedBox(height: 20),

                ElevatedButton(
                  onPressed: pickImage,
                  child: const Text(
                    "Choose Image",
                  ),
                ),

                ElevatedButton(
                  onPressed: testPlugin,
                  child: const Text(
                    "Test Plugin",
                  ),
                ),
              ],
            ),
          ),
        ),
      );
    }
  }
