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

      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,

          children: [

            if(selectedImage != null)

              Image.file(
                selectedImage!,
                width:200,
                height:200,
              ),
            
            Text(output),

            ElevatedButton(

              onPressed: pickImage,

              child: const Text(
                  "Choose Image"
              ),

            ),

            ElevatedButton(
              onPressed: testPlugin,

              child: const Text("Test Plugin"),
            ),
          ],
        ),
      ),
    );
  }
}
