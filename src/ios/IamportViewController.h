//
//  IamportViewController.h
//  IamportCordovaExample
//
//  Created by deedee on 2019/12/19.
//

#import <UIKit/UIKit.h>

@interface IamportViewController: UIViewController<UIWebViewDelegate>

@property (strong, nonatomic) IBOutlet UIWebView *webView;
@property () BOOL isWebViewLoaded;
@property (strong, nonatomic) NSString *type;
@property (strong, nonatomic) NSObject *params;

BOOL isOver(NSString *url);

@end
